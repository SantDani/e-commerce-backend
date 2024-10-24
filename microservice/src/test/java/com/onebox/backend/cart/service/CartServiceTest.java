package com.onebox.backend.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ErrorCode;
import com.onebox.backend.cart.model.Cart;
import com.onebox.backend.cart.model.Product;
import com.onebox.backend.cart.repository.CartRepository;
import com.onebox.backend.cart.utils.BusinessValidator;

public class CartServiceTest {

    @Mock
    private BusinessValidator productValidationService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCartSuccess() {
        Cart cart = new Cart();
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart savedCart = cartService.createCart();
        assertNotNull(savedCart);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testGetCartByIdSuccess() {
        Cart cart = new Cart();
        String validId = "1";
        cart.setId(validId);

        when(cartRepository.findById(validId)).thenReturn(cart);

        Cart foundCart = cartService.getCartById(validId);
        assertNotNull(foundCart);
        assertEquals(validId, foundCart.getId());
        verify(cartRepository, times(1)).findById(validId);
    }

    @Test
    public void testGetCartByIdCartNotFound() {
        String noExistentId = "1";
        when(cartRepository.findById(noExistentId)).thenReturn(null);

        CartException exception = assertThrows(CartException.class, () -> {
            cartService.getCartById(noExistentId);
        });

        ErrorCode cartNotFound = ErrorCode.CART_NOT_FOUND;

        assertEquals(cartNotFound.getMessage() + " ID: " + noExistentId, exception.getMessage());
        assertEquals(cartNotFound.getCode(), exception.getCode());
        assertEquals(cartNotFound.getHttpStatus(), exception.getHttpStatus());
    }

    @Test
    public void testGetCartById_LimitCases() {
        CartException exception = assertThrows(CartException.class, () -> {
            cartService.getCartById(null);
        });

        ErrorCode cartNotFound = ErrorCode.CART_NOT_FOUND;
        assertEquals(cartNotFound.getMessage(), exception.getMessage());
        assertEquals(cartNotFound.getCode(), exception.getCode());
        assertEquals(cartNotFound.getHttpStatus(), exception.getHttpStatus());

        exception = assertThrows(CartException.class, () -> {
            cartService.getCartById("");
        });

        assertEquals(cartNotFound.getMessage(), exception.getMessage());
        assertEquals(cartNotFound.getCode(), exception.getCode());
        assertEquals(cartNotFound.getHttpStatus(), exception.getHttpStatus());

        // Ensure the repository method was never called since the input was invalid
        verify(cartRepository, times(0)).findById(anyString());
    }

    @Test
    public void testGetAllCartsSuccess() {

        Cart cart1 = new Cart();
        cart1.setId("1");

        Cart cart2 = new Cart();
        cart2.setId("2");

        List<Cart> carts = Arrays.asList(cart1, cart2);
        when(cartRepository.findAll()).thenReturn(carts);

        List<Cart> retrievedCarts = cartService.getAllsCarts();

        assertNotNull(retrievedCarts);
        assertEquals(2, retrievedCarts.size());
        assertEquals("1", retrievedCarts.get(0).getId());
        assertEquals("2", retrievedCarts.get(1).getId());
        verify(cartRepository, times(1)).findAll();
    }

    public void testGetAllCartsLimitCases() {
        // It's not necessary mock empty list since the repository method will return an
        // empty list by default

        List<Cart> retrievedCarts = cartService.getAllsCarts();

        assertNotNull(retrievedCarts);
        assertTrue(retrievedCarts.isEmpty());
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    public void testAddProductsToCartSuccess() {

        String cartId = "1";
        Cart cart = new Cart();
        cart.setId(cartId);

        Product product1 = new Product(1, "Product1", 10);
        Product product2 = new Product(2, "Product2", 5);

        when(cartRepository.findById(cartId)).thenReturn(cart);
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.addProductsToCart(cartId, Arrays.asList(product1, product2));

        assertEquals(2, cart.getProducts().size());
        Product product1InCart = cart.getProducts().stream()
                .filter(p -> p.getId().equals(1))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product 1 not found"));

        Product product2InCart = cart.getProducts().stream()
                .filter(p -> p.getId().equals(2))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product 2 not found"));

        assertEquals(10, product1InCart.getAmount());
        assertEquals(5, product2InCart.getAmount());
        assertEquals("Product1", product1InCart.getDescription());
        assertEquals("Product2", product2InCart.getDescription());

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
        verify(productValidationService, times(2)).validateProduct(any(Product.class));
    }

    @Test
    public void testAddProductsToCartEmptyNullProductList() {
        String cartId = "1";
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(cart);

        CartException exception = assertThrows(CartException.class, () -> {
            cartService.addProductsToCart(cartId, null);
        });

        ErrorCode errorCode = ErrorCode.PRODUCT_NULL_EMPTY;
        assertEquals(errorCode.getMessage(), exception.getMessage());
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(errorCode.getHttpStatus(), exception.getHttpStatus());

        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(cart);

        exception = assertThrows(CartException.class, () -> {
            cartService.addProductsToCart(cartId, Collections.emptyList());
        });

        errorCode = ErrorCode.PRODUCT_NULL_EMPTY;
        assertEquals(errorCode.getMessage(), exception.getMessage());
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(errorCode.getHttpStatus(), exception.getHttpStatus());

        verify(cartRepository, times(0)).save(any(Cart.class));

    }

    @Test
    public void testAddProductsToCartLimitCases() {
        // Negative case
        String cartId = "1";
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(cart);

        Product productWithNegativeAmount = new Product(1, "Product1", -5);
        Product validProduct = new Product(2, "Product2", 5);

        CartException exception = assertThrows(CartException.class, () -> {
            cartService.addProductsToCart(cartId, Arrays.asList(productWithNegativeAmount, validProduct));
        });

        ErrorCode amountNegative = ErrorCode.AMOUNT_NEGATIVE;
        assertEquals(amountNegative.getMessage() + " ID: " + cartId, exception.getMessage());
        assertEquals(amountNegative.getCode(), exception.getCode());
        assertEquals(amountNegative.getHttpStatus(), exception.getHttpStatus());

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(0)).save(any(Cart.class));

        // Case not found
        String invalidCartId = "999";

        when(cartRepository.findById(invalidCartId)).thenReturn(null);

        List<Product> products = Arrays.asList(
                new Product(1, "Product 1", 10),
                new Product(2, "Product 2", 5));

        exception = assertThrows(CartException.class, () -> {
            cartService.addProductsToCart(invalidCartId, products);
        });

        ErrorCode cartNotFound = ErrorCode.CART_NOT_FOUND;
        assertEquals(cartNotFound.getMessage() + " ID: " + invalidCartId, exception.getMessage());
        assertEquals(cartNotFound.getCode(), exception.getCode());
        assertEquals(cartNotFound.getHttpStatus(), exception.getHttpStatus());

        verify(cartRepository, times(1)).findById(invalidCartId);
        verify(cartRepository, times(0)).save(any(Cart.class));
    }

    @Test
    public void testAddProductsToCartProductAlreadyExists() {
        String cartId = "1";
        Cart cart = new Cart();
        cart.setId(cartId);

        Product existingProduct = new Product(1, "Product1", 10);
        cart.getProducts().add(existingProduct);

        Product newProductToAdd = new Product(1, "Product1", 5);

        when(cartRepository.findById(cartId)).thenReturn(cart);
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.addProductsToCart(cartId, List.of(newProductToAdd));

        assertEquals(1, cart.getProducts().size());
        assertEquals(15, cart.getProducts().iterator().next().getAmount());

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
        verify(productValidationService, times(1)).validateProduct(any(Product.class));
    }

    @Test
    public void testDeleteCartSuccess() {

        String validId = "1";
        Cart cart = new Cart();
        cart.setId(validId);

        when(cartRepository.findById(validId)).thenReturn(cart);

        cartService.deleteCart(validId);

        verify(cartRepository, times(1)).findById(validId);
        verify(cartRepository, times(1)).deleteById(validId);
    }

    @Test
    public void testDeleteCartCartNotFound() {

        String invalidId = "999";
        when(cartRepository.findById(invalidId)).thenReturn(null);

        // When & Then
        CartException exception = assertThrows(CartException.class, () -> {
            cartService.deleteCart(invalidId); // This should throw CartNotFoundException
        });

        ErrorCode cartNotFound = ErrorCode.CART_NOT_FOUND;
        assertEquals(cartNotFound.getMessage() + " ID: " + invalidId, exception.getMessage());
        assertEquals(cartNotFound.getCode(), exception.getCode());
        assertEquals(cartNotFound.getHttpStatus(), exception.getHttpStatus());

        verify(cartRepository, times(1)).findById(invalidId);
        verify(cartRepository, times(0)).deleteById(anyString());
    }

    @Test
    public void testRemoveInactiveCartsSuccess() {

        ReflectionTestUtils.setField(cartService, "inactivityMinutes", 10);

        LocalDateTime now = LocalDateTime.now();

        Cart inactiveCart = new Cart();
        inactiveCart.setId("1");
        inactiveCart.setLastAccessed(now.minusMinutes(15));

        Cart activeCart = new Cart();
        activeCart.setId("2");
        activeCart.setLastAccessed(now.minusMinutes(5));

        List<Cart> carts = Arrays.asList(inactiveCart, activeCart);
        when(cartRepository.findAll()).thenReturn(carts);

        cartService.removeInactiveCarts();

        verify(cartRepository, times(1)).deleteByIds(List.of("1"));

        verify(cartRepository, times(1)).findAll();
    }

    @Test
    public void testRemoveInactiveCartsLimitCasesNoInactiveCarts() {

        ReflectionTestUtils.setField(cartService, "inactivityMinutes", 10);

        LocalDateTime now = LocalDateTime.now();
        Cart activeCart = new Cart();
        activeCart.setId("1");
        activeCart.setLastAccessed(now.minusMinutes(5));

        List<Cart> activeCarts = Collections.singletonList(activeCart);
        when(cartRepository.findAll()).thenReturn(activeCarts);

        cartService.removeInactiveCarts();

        verify(cartRepository, times(0)).deleteByIds(anyList());

    }

}
