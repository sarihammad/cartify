package com.example.cartify.payment.model;

import com.example.cartify.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripeRefundId;
    private String paymentIntentId;
    private Long amount;
    private String currency;
    private String status;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}