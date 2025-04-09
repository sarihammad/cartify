package com.example.cartify.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefundService {

    public Refund createRefund(String paymentIntentId) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .build();

            Refund refund = Refund.create(params);
            log.info("Refund created: {}", refund.getId());
            return refund;

        } catch (StripeException e) {
            log.error("Failed to create refund", e);
            throw new RuntimeException("Refund failed: " + e.getMessage());
        }
    }
}