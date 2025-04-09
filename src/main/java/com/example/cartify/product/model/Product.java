package com.example.cartify.product.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @Column(length = 1000)
    private String description;

    private BigDecimal price;

    private String imageUrl;

}