package com.example.cartify.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cartify.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}