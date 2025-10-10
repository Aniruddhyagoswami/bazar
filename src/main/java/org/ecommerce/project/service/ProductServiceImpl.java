package org.ecommerce.project.service;

import org.ecommerce.project.execptions.APIExecption;
import org.ecommerce.project.execptions.ResourceNotFoundException;
import org.ecommerce.project.model.Cart;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.model.Product;
import org.ecommerce.project.payload.CartDTO;
import org.ecommerce.project.payload.ProductDTO;
import org.ecommerce.project.payload.ProductResponse;
import org.ecommerce.project.repository.CartRepository;
import org.ecommerce.project.repository.CategoryRepository;
import org.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        boolean isProductNotPresent=true;
        List<Product> products=category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw  new APIExecption("Product already exists");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> products=productRepository.findAll(pageDetails);

        List<Product> productList=products.getContent();
        List<ProductDTO> productDTOS=productList.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getNumberOfElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());


        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> products=productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);

        List<Product> productList=products.getContent();

        List<ProductDTO> productDTOS=products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getNumberOfElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> products=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);

       List<ProductDTO> productDTOS=products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getNumberOfElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(productDTO, Product.class);

        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct = productRepository.save(productFromDb);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }
    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // DELETE
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        String fileName=fileService.uploadImage(path,image);
        productFromDb.setImage(fileName);
        Product savedProduct=productRepository.save(productFromDb);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }



}
