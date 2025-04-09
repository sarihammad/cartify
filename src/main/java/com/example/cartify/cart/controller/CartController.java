package com.example.cartify.cart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private User getUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(
            Principal principal,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        cartService.addToCart(getUser(principal), productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(Principal principal) {
        return ResponseEntity.ok(cartService.getCartItems(getUser(principal)));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(
            Principal principal,
            @RequestParam Long productId
    ) {
        cartService.removeItem(getUser(principal), productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Principal principal) {
        cartService.clearCart(getUser(principal));
        return ResponseEntity.noContent().build();
    }
}