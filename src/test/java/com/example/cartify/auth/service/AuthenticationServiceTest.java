package com.example.cartify.auth.service;

import com.example.cartify.auth.dto.RegisterRequest;
import com.example.cartify.auth.model.Role;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.payment.service.StripeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final StripeService stripeService = mock(StripeService.class);

    private final AuthenticationService authService = new AuthenticationService(
            userRepository,
            passwordEncoder,
            jwtService,
            authenticationManager,
            stripeService
    );

    @Test
    void testRegister_ShouldCreateUserAndReturnToken() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("secret")
                .build();

        String encodedPassword = "encoded_secret";
        String jwtToken = "jwt_token";

        when(passwordEncoder.encode("secret")).thenReturn(encodedPassword);
        when(jwtService.generateToken("john@example.com")).thenReturn(jwtToken);
        when(stripeService.createCustomer("john@example.com")).thenReturn("cus_mock_123");

        var response = authService.register(request);

        verify(userRepository).save(Mockito.argThat(user ->
                user.getFirstname().equals("John") &&
                user.getEmail().equals("john@example.com") &&
                user.getPassword().equals(encodedPassword) &&
                user.getRole().equals(Role.USER) &&
                user.getStripeCustomerId().equals("cus_mock_123")
        ));

        assertThat(response.getToken()).isEqualTo(jwtToken);
    }
}