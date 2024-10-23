package com.onebox.backend.cart.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ErrorCode;
import com.onebox.backend.cart.model.Cart;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class CartRepositoryTest {
    private CartRepository cartRepository;

    @BeforeEach
    public void setup() {
        cartRepository = new CartRepository();
    }

    @Test
    public void testSaveCartSuccess() {
        Cart cart = new Cart();
        cart.setId("1");

        Cart savedCart = cartRepository.save(cart);

        assertNotNull(savedCart);
        assertEquals("1", savedCart.getId());
    }

    @Test
    public void testSaveCartNull() {
        Cart savedCart = cartRepository.save(null);

        assertNull(savedCart);
    }

    @Test
    public void testSaveCartNullId() {
        Cart cart = new Cart();
        cart.setId(null);

        Cart savedCart = cartRepository.save(cart);

        assertNull(savedCart);
    }

    @Test
    public void testFindByIdSuccess() {
        Cart cart = new Cart();
        cart.setId("1");
        cartRepository.save(cart);

        Cart foundCart = cartRepository.findById("1");

        assertNotNull(foundCart);
        assertEquals("1", foundCart.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Cart foundCart = cartRepository.findById("999");

        assertNull(foundCart);
    }

    @Test
    public void testFindByIdNullEmpty() {
        Cart foundCart = cartRepository.findById(null);
        assertNull(foundCart);

        foundCart = cartRepository.findById("");

        assertNull(foundCart);
    }

    @Test
    public void testDeleteByIdSuccess() {
        Cart cart = new Cart();
        cart.setId("1");
        cartRepository.save(cart);

        cartRepository.deleteById("1");

        Cart foundCart = cartRepository.findById("1");
        assertNull(foundCart);
    }

    @Test
    public void testDeleteByIdNullOrEmpty() {
        CartException exception = assertThrows(CartException.class, () -> cartRepository.deleteById(null));

        assertEquals(ErrorCode.CART_NULL_EMPTY.getMessage(), exception.getMessage());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getCode(), exception.getCode());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getHttpStatus(), exception.getHttpStatus());

        exception = assertThrows(CartException.class, () -> cartRepository.deleteById(""));
        assertEquals(ErrorCode.CART_NULL_EMPTY.getMessage(), exception.getMessage());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getCode(), exception.getCode());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getHttpStatus(), exception.getHttpStatus());
    }

    @Test
    public void testDeleteByIdCartNotFound() {
        CartException exception = assertThrows(CartException.class, () -> cartRepository.deleteById("999"));

        assertTrue(exception.getMessage().contains("ID: 999"));
        assertEquals(ErrorCode.CART_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(ErrorCode.CART_NOT_FOUND.getHttpStatus(), exception.getHttpStatus());
    }

    @Test
    public void testFindAllSuccess() {

        Cart cart1 = new Cart();
        cart1.setId("1");

        Cart cart2 = new Cart();
        cart2.setId("2");

        cartRepository.save(cart1);
        cartRepository.save(cart2);

        List<Cart> carts = cartRepository.findAll();

        assertNotNull(carts);
        assertEquals(2, carts.size());
    }

    @Test
    public void testDeleteByIdsSuccess() {
        Cart cart1 = new Cart();
        cart1.setId("1");
        Cart cart2 = new Cart();
        cart2.setId("2");

        cartRepository.save(cart1);
        cartRepository.save(cart2);

        cartRepository.deleteByIds(Arrays.asList("1", "2"));

        assertNull(cartRepository.findById("1"));
        assertNull(cartRepository.findById("2"));
    }

    @Test
    public void testDeleteByIdsNull() {
        CartException exception = assertThrows(CartException.class, () -> cartRepository.deleteByIds(null));

        assertEquals(ErrorCode.CART_NULL_EMPTY.getMessage(), exception.getMessage());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getCode(), exception.getCode());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getHttpStatus(), exception.getHttpStatus());
    }

    @Test
    public void testDeleteByIdsEmptyList() {
        CartException exception = assertThrows(CartException.class,
                () -> cartRepository.deleteByIds(Collections.emptyList()));

        assertEquals(ErrorCode.CART_NULL_EMPTY.getMessage(), exception.getMessage());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getCode(), exception.getCode());
        assertEquals(ErrorCode.CART_NULL_EMPTY.getHttpStatus(), exception.getHttpStatus());
    }

    @Test
    public void testDeleteByIdsWithNonExistentId() {
        Cart cart1 = new Cart();
        cart1.setId("1");
        Cart cart2 = new Cart();
        cart2.setId("2");

        cartRepository.save(cart1);
        cartRepository.save(cart2);

        CartException exception = assertThrows(CartException.class,
                () -> cartRepository.deleteByIds(Arrays.asList("1", "999")));

        assertTrue(exception.getMessage().contains("ID: 999"));
        assertEquals(ErrorCode.CART_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(ErrorCode.CART_NOT_FOUND.getHttpStatus(), exception.getHttpStatus());

        assertNull(cartRepository.findById("1"));
        assertNotNull(cartRepository.findById("2"));
    }

}
