package com.example.cartify.payment.service;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.payment.model.Refund;
import com.example.cartify.payment.repository.RefundRepository;
import com.stripe.exception.StripeException;
// import com.stripe.model.Refund as StripeRefund;
import com.stripe.param.RefundCreateParams;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final UserRepository userRepository;

    public Refund createRefund(String email, String paymentIntentId, Long amountInCents) throws StripeException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(paymentIntentId)
                .setAmount(amountInCents)
                .build();

        com.stripe.model.Refund stripeRefund = com.stripe.model.Refund.create(params);

        Refund refund = Refund.builder()
                .stripeRefundId(stripeRefund.getId())
                .paymentIntentId(paymentIntentId)
                .amount(stripeRefund.getAmount())
                .currency(stripeRefund.getCurrency())
                .status(stripeRefund.getStatus())
                .user(user)
                .createdAt(Instant.now())
                .build();

        return refundRepository.save(refund);
    }
}