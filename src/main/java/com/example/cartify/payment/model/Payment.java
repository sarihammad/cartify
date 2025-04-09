package com.example.cartify.payment.model;

import com.example.cartify.auth.model.User;
import com.example.cartify.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private BigDecimal amount;

    private String paymentMethod;

    private LocalDateTime paidAt;

    @ManyToOne
    private User user;

    @OneToOne
    private Order order;
}