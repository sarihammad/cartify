package com.example.cartify.coupon.repository;

import com.example.cartify.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCodeAndIsActiveTrue(String code);
}