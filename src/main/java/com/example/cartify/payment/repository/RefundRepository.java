package com.example.cartify.payment.repository;

import com.example.cartify.payment.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}