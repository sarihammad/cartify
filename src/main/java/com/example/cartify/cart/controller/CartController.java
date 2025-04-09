package com.example.cartify.cart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cartify.cart.dto.CartItemRequest;
import com.example.cartify.cart.dto.CartItemResponse;
import com.example.cartify.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(
            Principal principal,
            @RequestBody CartItemRequest request
    ) {
        cartService.addToCart(principal.getName(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(Principal principal) {
        return ResponseEntity.ok(cartService.getCartItems(principal.getName()));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(
            Principal principal,
            @RequestParam Long productId
    ) {
        cartService.removeItem(principal.getName(), productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return ResponseEntity.noContent().build();
    }
}