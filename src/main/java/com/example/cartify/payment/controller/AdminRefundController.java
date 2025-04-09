package com.example.cartify.payment.controller;

import com.example.cartify.payment.service.RefundService;
import com.stripe.exception.StripeException;
import com.example.cartify.payment.model.Refund;
// import com.stripe.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/refunds")
@RequiredArgsConstructor
public class AdminRefundController {

    private final RefundService refundService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> refundPayment(@RequestParam String paymentIntentId) {
        try {
            Refund refund = refundService.createRefund(paymentIntentId);
            return ResponseEntity.ok(refund);
        } catch (StripeException e) {
            return ResponseEntity
                    .status(500)
                    .body("Stripe API error: " + e.getMessage());
        }
    }
}