package org.ecommerce.project.service;

import org.ecommerce.project.payload.CartDTO;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
