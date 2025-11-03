package org.ecommerce.project.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ecommerce.project.model.Cart;
import org.ecommerce.project.payload.CartDTO;
import org.ecommerce.project.repository.CartRepository;
import org.ecommerce.project.service.CartService;
import org.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {


    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private CartService cartService;
    @Autowired
    private AuthUtil authUtil;
    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(
            summary = "Add product to cart",
            description = "Adds a product with a specific quantity to the logged-in user's cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID or quantity"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable
            Long productId,
            @PathVariable
            Integer quantity
            ){
        CartDTO cartDTO=cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO,HttpStatus.CREATED);
    }
    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(
            summary = "Retrieve all carts",
            description = "Fetches a list of all shopping carts. Usually for admin use."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carts fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOs=cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }
    @Tag(name = "Cart APIs",description = "APIs for managing carts")
    @Operation(
            summary = "Get current user's cart",
            description = "Fetches the cart of the currently logged-in user using their authenticated email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found for the user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId= authUtil.loggedInEmail();
        Cart cart=cartRepository.findCartByEmail(emailId);
        Long cartId=cart.getCartId();
        CartDTO cartDTO=cartService.getCart(emailId,cartId);
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(
            summary = "Update product quantity in cart",
            description = "Increases or decreases a product quantity in the cart using an operation parameter ('increase' or 'delete')."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID or operation"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart")
    })
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId, @PathVariable String operation){
        CartDTO cartDTO=cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }
    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(
            summary = "Delete product from cart",
            description = "Removes a product from the cart by cart ID and product ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted from cart successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,@PathVariable Long productId){
        String status=cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<String>(status,HttpStatus.OK);
    }


}
