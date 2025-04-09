package com.example.cartify.coupon.service;

import com.example.cartify.coupon.model.Coupon;
import com.example.cartify.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Invalid or inactive coupon"));

        if (coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Coupon has expired");
        }

        return coupon;
    }

    public BigDecimal applyDiscount(BigDecimal total, Coupon coupon) {
        return total.subtract(total.multiply(coupon.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
    }
}