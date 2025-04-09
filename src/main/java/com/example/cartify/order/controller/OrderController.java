package com.example.cartify.order.controller;

import com.example.cartify.order.dto.OrderResponse;
import com.example.cartify.order.dto.PlaceOrderRequest;
import com.example.cartify.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(Principal principal, @RequestBody(required = false) PlaceOrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(principal.getName(), request != null ? request.getCouponCode() : null));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getUserOrders(principal.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}