package com.example.cartify.order.model;

import com.example.cartify.auth.model.User;
import com.example.cartify.coupon.model.Coupon;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private BigDecimal total;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // cascade to 
    private List<OrderItem> items;

    private Coupon coupon;
}