package com.example.cartify.product.controller;

import com.example.cartify.config.JwtAuthenticationFilter;
import com.example.cartify.config.StripeConfig;
import com.example.cartify.payment.service.PaymentService;
// import com.example.cartify.config.RedissonConfig;
// import com.example.cartify.product.controller.ProductControllerTest.TestRedisConfig;
import com.example.cartify.product.dto.ProductRequest;
import com.example.cartify.product.dto.ProductResponse;
import com.example.cartify.product.model.Product;
import com.example.cartify.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.AsyncBucketProxy;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;

import org.junit.jupiter.api.*;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.*;
import io.github.bucket4j.distributed.proxy.RemoteAsyncBucketBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("cartify_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setupMocks() {
        var mockBuilder = mock(RemoteAsyncBucketBuilder.class);
        when(asyncProxyManager.builder()).thenReturn(mockBuilder);
        when(mockBuilder.build(any(String.class), any(BucketConfiguration.class)))
    .thenReturn(mock(AsyncBucketProxy.class));
    }

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private RedissonClient redissonClient;

    @MockBean
    private AsyncProxyManager<String> asyncProxyManager;

    @MockBean
    private StripeConfig stripeConfig;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @WithMockUser(roles = "ADMIN")
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(20))
                .imageUrl("http://example.com/image.png")
                .quantity(10)
                .build();

        ProductResponse productResponse = ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(20))
                .imageUrl("http://example.com/image.png")
                .quantity(10)
                .build();

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @Order(2)
    @WithMockUser(roles = "ADMIN")
    void getAllProducts_ShouldReturnProductList() throws Exception {
        List<ProductResponse> mockedList = List.of(
            ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(20))
                .imageUrl("http://example.com/image.png")
                .quantity(10)
                .build()
        );
    
        when(productService.getAllProducts()).thenReturn(mockedList);
    
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}

