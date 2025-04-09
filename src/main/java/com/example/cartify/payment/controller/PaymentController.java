package com.example.cartify.payment.controller;

import com.example.cartify.payment.dto.CreatePaymentRequest;
import com.example.cartify.payment.dto.CreatePaymentResponse;
import com.example.cartify.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) throws StripeException {
        String clientSecret = paymentService.createPaymentIntent(request.getAmount(), request.getCurrency());
        return ResponseEntity.ok(CreatePaymentResponse.builder()
                .clientSecret(clientSecret)
                .build());
    }
}