package com.example.cartify.cart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cartify.auth.model.User;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.cart.repository.CartItemRepository;
import com.example.cartify.product.model.Product;
import com.example.cartify.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    public void addToCart(User user, Long productId, int quantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        cartRepo.save(item);
    }

    public List<CartItem> getCartItems(User user) {
        return cartRepo.findByUser(user);
    }

    public void removeItem(User user, Long productId) {
        cartRepo.deleteByUserAndProductId(user, productId);
    }

    public void clearCart(User user) {
        cartRepo.findByUser(user).forEach(cartRepo::delete);
    }
}