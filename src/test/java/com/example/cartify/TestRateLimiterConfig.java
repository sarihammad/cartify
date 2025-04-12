package com.example.cartify;

import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.example.cartify.config.RateLimiterFilter;

@TestConfiguration
public class TestRateLimiterConfig {

    @Bean
    public AsyncProxyManager<String> asyncProxyManagerMock() {
        return Mockito.mock(AsyncProxyManager.class);
    }

    @Bean
    @Primary
    public RateLimiterFilter testRateLimiterFilter() {
        return new RateLimiterFilter(null) {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws IOException {
                try {
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}




