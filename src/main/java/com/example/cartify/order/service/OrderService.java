package com.example.cartify.order.service;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.cart.repository.CartItemRepository;
import com.example.cartify.coupon.model.Coupon;
import com.example.cartify.coupon.service.CouponService;
import com.example.cartify.order.dto.OrderItemResponse;
import com.example.cartify.order.dto.OrderResponse;
import com.example.cartify.order.model.Order;
import com.example.cartify.order.model.OrderItem;
import com.example.cartify.order.repository.OrderRepository;
import com.example.cartify.product.model.Product;
import com.example.cartify.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CouponService couponService;

    public OrderResponse placeOrder(String email, String couponCode) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Coupon coupon = null;
        if (couponCode != null && !couponCode.isBlank()) {
            coupon = couponService.validateCoupon(couponCode);
            total = couponService.applyDiscount(total, coupon);
        }

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .total(total)
                .coupon(coupon)
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(item -> {
            Product product = item.getProduct();

            // Check and reduce stock
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - item.getQuantity());

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .priceSnapshot(product.getPrice())
                    .build();
        }).toList();

        order.setItems(orderItems);
        orderRepository.save(order);

        // Save updated stock
        orderItems.forEach(orderItem -> {
            productRepository.save(orderItem.getProduct());
        });

        // Clear cart after placing order
        cartItemRepository.deleteAll(cartItems);

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

    public Order findLatestOrderByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        return orderRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new RuntimeException("No order found for user"));
    }
}