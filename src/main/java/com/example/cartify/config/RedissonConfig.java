package com.example.cartify.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        System.out.println("Using manual RedissonClient config");
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer()
              .setAddress("redis://cartify_redis:6379");
        return Redisson.create(config);
    }
}