package com.example.cartify.cart.model;

import com.example.cartify.product.model.Product;
import com.example.cartify.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity 
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CartItem {

    @Id // Mark this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the I
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;

    private int quantity;
}