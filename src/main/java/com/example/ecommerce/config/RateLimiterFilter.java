package com.example.ecommerce.config;

import io.github.bucket4j.*;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {

    private final RedissonClient redissonClient;

    private final static int REQUEST_LIMIT = 5;
    private final static Duration DURATION = Duration.ofMinutes(1);
    private final static String RATE_LIMIT_PREFIX = "rate_limit:";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String key = RATE_LIMIT_PREFIX + ip;

        RMapCache<String, Bucket> cache = redissonClient.getMapCache("buckets");
        Bucket bucket = cache.get(key);

        if (bucket == null) {
            Refill refill = Refill.greedy(REQUEST_LIMIT, DURATION);
            Bandwidth limit = Bandwidth.classic(REQUEST_LIMIT, refill);
            bucket = Bucket4j.builder().addLimit(limit).build();
            cache.put(key, bucket, DURATION.toMillis(), TimeUnit.MILLISECONDS);
        }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - try again later");
        }
    }
}