package org.ecommerce.project.service;

import org.ecommerce.project.payload.CartDTO;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quantity);
}
