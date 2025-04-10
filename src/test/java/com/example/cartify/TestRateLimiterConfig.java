package com.example.cartify;

import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestRateLimiterConfig {

    @Bean
    public AsyncProxyManager<String> asyncProxyManagerMock() {
        return Mockito.mock(AsyncProxyManager.class);
    }
}