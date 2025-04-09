package com.example.cartify.order.service;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.cart.repository.CartItemRepository;
import com.example.cartify.order.dto.OrderItemResponse;
import com.example.cartify.order.dto.OrderResponse;
import com.example.cartify.order.model.Order;
import com.example.cartify.order.model.OrderItem;
import com.example.cartify.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderResponse placeOrder(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .total(total)
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(item -> OrderItem.builder()
                .order(order)
                .product(item.getProduct())
                .quantity(item.getQuantity())
                .priceSnapshot(item.getProduct().getPrice())
                .build()).toList();

        order.setItems(orderItems);
        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems); // Clear cart after placing order

        return toDto(order);
    }

    public List<OrderResponse> getUserOrders(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return orderRepository.findByUser(user).stream().map(this::toDto).toList();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDto).toList();
    }

    private OrderResponse toDto(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(item -> OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .priceSnapshot(item.getPriceSnapshot())
                .build()).toList();

        return OrderResponse.builder()
                .id(order.getId())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}