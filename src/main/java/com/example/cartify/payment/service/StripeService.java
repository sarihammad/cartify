package com.example.cartify.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public String createCustomer(String email) {
        try {
            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(email)
                    .build();

            Customer customer = Customer.create(params);
            return customer.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Stripe customer creation failed", e);
        }
    }
}