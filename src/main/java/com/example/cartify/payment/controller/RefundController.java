package com.example.cartify.payment.controller;

import com.example.cartify.payment.service.RefundService;
import com.stripe.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Refund> refundPayment(@RequestParam String paymentIntentId) {
        return ResponseEntity.ok(refundService.createRefund(paymentIntentId));
    }
}