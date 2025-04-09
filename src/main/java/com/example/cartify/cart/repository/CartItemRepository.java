package com.example.cartify.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.auth.model.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUserAndProductId(User user, Long productId);
}