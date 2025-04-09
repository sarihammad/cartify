package com.example.cartify.payment.controller;

import com.example.cartify.payment.dto.PaymentRequest;
import com.example.cartify.payment.dto.PaymentResponse;
import com.example.cartify.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(
            Principal principal,
            @RequestBody PaymentRequest request
    ) {
        return ResponseEntity.ok(paymentService.makePayment(principal.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getUserPayments(Principal principal) {
        return ResponseEntity.ok(paymentService.getUserPayments(principal.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}