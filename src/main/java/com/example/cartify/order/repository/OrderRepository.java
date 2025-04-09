package com.example.cartify.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cartify.auth.model.User;
import com.example.cartify.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findTopByUserOrderByCreatedAtDesc(User user);
}