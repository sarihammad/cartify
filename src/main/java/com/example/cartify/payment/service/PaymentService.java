package com.example.cartify.payment.service;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.order.model.Order;
import com.example.cartify.order.repository.OrderRepository;
import com.example.cartify.payment.dto.PaymentRequest;
import com.example.cartify.payment.dto.PaymentResponse;
import com.example.cartify.payment.model.Payment;
import com.example.cartify.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public PaymentResponse makePayment(String email, PaymentRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized payment attempt.");
        }

        Payment payment = Payment.builder()
                .user(user)
                .order(order)
                .amount(order.getTotal())
                .paymentMethod(request.getPaymentMethod())
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return toDto(payment);
    }

    public List<PaymentResponse> getUserPayments(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return paymentRepository.findByUser(user).stream().map(this::toDto).toList();
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    private PaymentResponse toDto(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paidAt(payment.getPaidAt())
                .orderId(payment.getOrder().getId())
                .build();
    }
}