package com.example.cartify.payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.example.cartify.order.model.Order;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripeSessionId;
    private String userEmail;
    private double amount;
    private String currency;
    private String status;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
}