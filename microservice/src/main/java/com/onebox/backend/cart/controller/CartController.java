package com.onebox.backend.cart.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onebox.backend.cart.model.Cart;
import com.onebox.backend.cart.model.Product;
import com.onebox.backend.cart.service.CartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * REST controller for managing shopping carts.
 */
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    /**
     * Constructor for injecting the CartService.
     *
     * @param cartService the service to manage cart operations
     */
    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Creates a new cart.
     *
     * @return HTTP response with the created cart
     */
    @PostMapping
    public ResponseEntity<Cart> createCart() {
        return ResponseEntity.ok(cartService.createCart());
    }

    /**
     * Retrieves a cart by its ID.
     *
     * @param cartId the ID of the cart to retrieve
     * @return HTTP response with the retrieved cart
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCardById(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    /**
     * Retrieves all carts.
     *
     * @return HTTP response with the list of all carts
     */
    @GetMapping("/all")
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllsCarts());
    }

    /**
     * Updates a cart by adding products to it.
     *
     * @param id       the ID of the cart to update
     * @param products the list of products to add to the cart
     * @return HTTP response with the updated cart
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable String id, @RequestBody List<Product> products) {
        return ResponseEntity.ok(cartService.addProductsToCart(id, products));
    }

    /**
     * Deletes a cart by its ID.
     *
     * @param id the ID of the cart to delete
     * @return HTTP response with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

}
