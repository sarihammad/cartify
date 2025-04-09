package com.example.cartify.payment.repository;

import com.example.cartify.auth.model.User;
import com.example.cartify.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
}