package org.ecommerce.project.service;

import org.ecommerce.project.execptions.APIExecption;
import org.ecommerce.project.execptions.ResourceNotFoundException;
import org.ecommerce.project.model.Cart;
import org.ecommerce.project.model.CartItem;
import org.ecommerce.project.model.Product;
import org.ecommerce.project.payload.CartDTO;
import org.ecommerce.project.repository.CartItemRepository;
import org.ecommerce.project.repository.CartRepository;
import org.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    AuthUtil authUtil;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart=createCart();

        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );
        if (cartItem!=null){
            throw new APIExecption("Product"+product.getProductName()+" already exists in cart");
        }

        if (product.getQuantity()==0){
            throw new APIExecption("Product"+product.getProductName()+" is out of stock");
        }

        if (product.getQuantity()<quantity){
            throw new APIExecption("Product "+product.getProductName()+" only has "+product.getQuantity()+" in stock");
        }


        CartItem newCartItem=new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        cartRepository.save(cart);

        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);




    }

    private Cart createCart(){
        Cart userCart=cartRepository.findByEmail((authUtil.loggedInEmail());
        if (userCart!=null){
            return userCart;
        }
        Cart cart =new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggerInUser());
        Cart newCart=cartRepository.save(cart);
        return newCart;
    }

}
