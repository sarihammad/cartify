package com.example.cartify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class CartifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartifyApplication.class, args);
	}

}
