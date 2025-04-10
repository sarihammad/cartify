package com.example.cartify.cart.service;

import com.example.cartify.auth.model.User;
import com.example.cartify.auth.repository.UserRepository;
import com.example.cartify.cart.model.CartItem;
import com.example.cartify.cart.repository.CartItemRepository;
import com.example.cartify.product.model.Product;
import com.example.cartify.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartItemRepository = mock(CartItemRepository.class);
        productRepository = mock(ProductRepository.class);
        userRepository = mock(UserRepository.class);
        cartService = new CartService(cartItemRepository, productRepository, userRepository);
    }

@Test
void addToCart_ShouldSaveCartItem() {
    User user = User.builder().id(1L).email("test@example.com").build();
    Product product = Product.builder().id(2L).name("Test Product").price(BigDecimal.valueOf(10)).build();

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
    when(productRepository.findById(2L)).thenReturn(Optional.of(product));

    cartService.addToCart(user.getEmail(), 2L, 3);

    ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
    verify(cartItemRepository).save(captor.capture());

    CartItem saved = captor.getValue();
    assertThat(saved.getProduct()).isEqualTo(product);
    assertThat(saved.getUser()).isEqualTo(user);
    assertThat(saved.getQuantity()).isEqualTo(3);
}

    @Test
    void getCartItems_ShouldReturnItems() {
        User user = User.builder().id(1L).email("test@example.com").build();
        Product product = Product.builder().id(2L).name("Test Product").price(BigDecimal.valueOf(20)).build();
        CartItem item = CartItem.builder()
                .id(100L)
                .user(user)
                .product(product)
                .quantity(2)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser(user)).thenReturn(List.of(item));

        var result = cartService.getCartItems(user.getEmail());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(2);
        assertThat(result.get(0).getProductName()).isEqualTo("Test Product");
    }

    @Test
    void removeItem_ShouldCallDelete() {
        User user = User.builder().id(1L).email("test@example.com").build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        cartService.removeItem(user.getEmail(), 99L);
        verify(cartItemRepository).deleteByUserAndProductId(user, 99L);
    }

    @Test
    void clearCart_ShouldDeleteAllUserItems() {
        User user = User.builder().id(1L).email("test@example.com").build();
        CartItem item = CartItem.builder().id(10L).user(user).build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser(user)).thenReturn(List.of(item));

        cartService.clearCart(user.getEmail());
        verify(cartItemRepository).delete(item);
    }
}