package com.example.cartify.cart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.cart.dto.CartItemResponse;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.cart.repository.CartItemRepository;
import com.example.cartify.product.model.Product;
import com.example.cartify.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public void addToCart(String email, Long productId, int quantity) {
        User user = getUserByEmail(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        cartRepository.save(item);
    }

    public List<CartItemResponse> getCartItems(String email) {
        User user = getUserByEmail(email);
        return cartRepository.findByUser(user).stream()
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getProduct().getPrice().doubleValue())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public void removeItem(String email, Long productId) {
        User user = getUserByEmail(email);
        cartRepository.deleteByUserAndProductId(user, productId);
    }

    public void clearCart(String email) {
        User user = getUserByEmail(email);
        cartRepository.findByUser(user).forEach(cartRepository::delete);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}