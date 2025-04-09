package com.example.cartify.product.dto;

import lombok.*;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    
    @Min(0)
    private Integer quantity;
}
