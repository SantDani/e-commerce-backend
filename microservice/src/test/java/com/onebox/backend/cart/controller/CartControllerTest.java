package com.onebox.backend.cart.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.onebox.backend.cart.model.Cart;
import com.onebox.backend.cart.model.Product;
import com.onebox.backend.cart.service.CartService;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCart() {
        Cart cart = new Cart();
        when(cartService.createCart()).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.createCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
        verify(cartService, times(1)).createCart();
    }

    @Test
    public void testGetCartById() {
        Cart cart = new Cart();
        cart.setId("1");
        when(cartService.getCartById("1")).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.getCardById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
        verify(cartService, times(1)).getCartById("1");
    }

    @Test
    public void testGetAllCarts() {
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        List<Cart> cartList = Arrays.asList(cart1, cart2);
        when(cartService.getAllsCarts()).thenReturn(cartList);

        ResponseEntity<List<Cart>> response = cartController.getAllCarts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartList, response.getBody());
        verify(cartService, times(1)).getAllsCarts();
    }

    @Test
    public void testUpdateCart() {
        Cart updatedCart = new Cart();
        updatedCart.setId("1");

        Product product1 = new Product(1, "Product1", 10);
        Product product2 = new Product(2, "Product2", 5);
        List<Product> products = Arrays.asList(product1, product2);

        when(cartService.addProductsToCart("1", products)).thenReturn(updatedCart);

        ResponseEntity<Cart> response = cartController.updateCart("1", products);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCart, response.getBody());
        verify(cartService, times(1)).addProductsToCart("1", products);
    }

    @Test
    public void testDeleteCart() {
        ResponseEntity<Void> response = cartController.deleteCart("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService, times(1)).deleteCart("1");
    }
}
