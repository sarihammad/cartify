package com.example.cartify.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.AsyncBucketProxy;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {

    private final AsyncProxyManager<String> proxyManager;

    private static final int REQUEST_LIMIT = 10;
    private static final Duration DURATION = Duration.ofMinutes(1);

    private static final BucketConfiguration CONFIG = BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(REQUEST_LIMIT, Refill.intervally(REQUEST_LIMIT, DURATION)))
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ipKey = "rate-limit:" + request.getRemoteAddr();

        AsyncBucketProxy bucket = proxyManager.builder().build(ipKey, CONFIG);

        bucket.tryConsume(1).thenAccept(consumed -> {
            try {
                if (consumed) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.getWriter().write("Rate limit exceeded. Try again later.");
                }
            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(throwable -> {
            try {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.getWriter().write("Rate limiter error: " + throwable.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }
}