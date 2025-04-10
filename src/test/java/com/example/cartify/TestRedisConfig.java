package com.example.cartify;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.redisson.api.RedissonClient;

@TestConfiguration
public class TestRedisConfig {

    @Bean
    public RedissonClient redissonClient() {
        return Mockito.mock(RedissonClient.class);
    }
}