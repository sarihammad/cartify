package com.example.cartify.auth.service;

import com.example.cartify.auth.model.Role;
import com.example.cartify.auth.model.User;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cartify.auth.dto.*;
import com.example.cartify.auth.repository.UserRepository;
import com.stripe.model.Customer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    

    public AuthResponse register(RegisterRequest request) {

        Customer stripeCustomer;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("email", request.getEmail());
            stripeCustomer = Customer.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Stripe customer creation failed", e);
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .stripeCustomerId(stripeCustomer.getId())
                .build();
        
        userRepository.save(user);
        var token = jwtService.generateToken(user.getEmail());
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var token = jwtService.generateToken(user.getEmail());
        return AuthResponse.builder().token(token).build();
    }
}
