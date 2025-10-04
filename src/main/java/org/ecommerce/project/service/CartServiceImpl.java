package org.ecommerce.project.service;

import org.ecommerce.project.execptions.APIExecption;
import org.ecommerce.project.execptions.ResourceNotFoundException;
import org.ecommerce.project.model.Cart;
import org.ecommerce.project.model.CartItem;
import org.ecommerce.project.model.Product;
import org.ecommerce.project.payload.CartDTO;
import org.ecommerce.project.payload.ProductDTO;
import org.ecommerce.project.repository.CartItemRepository;
import org.ecommerce.project.repository.CartRepository;
import org.ecommerce.project.repository.ProductRepository;
import org.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

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
        List<CartItem> cartItems=cart.getCartItems();
        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts=cartRepository.findAll();
        if(carts.size()==0){
            throw new APIExecption("No carts exist");
        }

        List<CartDTO> cartDTOS=carts.stream().map(cart->{
            CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> products=cart.getCartItems()
                    .stream().map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart=cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if (cart==null){
            throw new ResourceNotFoundException("Cart","cartid",cartId);
        }
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products=cart.getCartItems()
                .stream().map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).collect(Collectors.toList());
        cartDTO.setProducts(products);

        return cartDTO;
    }


    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId=authUtil.loggedInEmail();
        Cart userCart=cartRepository.findCartByEmail(emailId);
        Long cartId=userCart.getCartId();
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart","CartId",cartId));
        Product product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        if (product.getQuantity()==0){
            throw new APIExecption("Product"+product.getProductName()+" is out of stock");
        }
        if (product.getQuantity()<quantity){
            throw new APIExecption("Product "+product.getProductName()+" only has "+product.getQuantity()+" in stock");

        }

        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if (cartItem==null){
            throw new APIExecption("Product"+product.getProductName()+" does not exist in cart");
        }
        int newQuantity=cartItem.getQuantity()+quantity;
        if (newQuantity<0){
            throw new APIExecption("Product"+product.getProductName()+" can not have negative quantity");
        }
        if (newQuantity==0){
            deleteProductFromCart(cartId,productId);

        }else {
            product.setQuantity(product.getQuantity()-quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getProductPrice()*quantity));
            cartRepository.save(cart);

        }

        CartItem updatedCartItem=cartItemRepository.save(cartItem);
        if (updatedCartItem.getQuantity()==0){
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems=cart.getCartItems();
        Stream<ProductDTO> productDTOStream=cartItems.stream()
                .map(item->{
                    ProductDTO prd=modelMapper.map(item.getProduct(),ProductDTO.class);
                    prd.setQuantity(item.getQuantity());
                    return prd;
                });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }
    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart","CartId",cartId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if (cartItem==null){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()- (cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);

        return "Product "+cartItem.getProduct().getProductName()+" remove from cart";
    }

    private Cart createCart(){
        Cart userCart=cartRepository.findCartByEmail((authUtil.loggedInEmail()));
        if (userCart!=null){
            return userCart;
        }
        Cart cart =new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart=cartRepository.save(cart);
        return newCart;
    }

}
