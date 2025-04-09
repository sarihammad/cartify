package com.example.ecommerce.config;

import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.github.bucket4j.redis.redisson.Bucket4jRedisson;
// import io.github.bucket4j.redis.redisson.cas.*;
// import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager.RedissonBasedProxyManagerBuilder;

import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bucket4jConfig {

    @Bean
    public AsyncProxyManager<String> asyncProxyManager(RedissonClient redissonClient) {
        // Get the command executor from Redisson's internal API
        CommandAsyncExecutor commandExecutor = ((org.redisson.Redisson) redissonClient).getCommandExecutor();

        // Build the proxy manager using the executor
        io.github.bucket4j.redis.redisson.Bucket4jRedisson.RedissonBasedProxyManagerBuilder<String> builder = Bucket4jRedisson.casBasedBuilder(commandExecutor);

        // Return the async proxy manager
        return builder.build().asAsync();
    }
}