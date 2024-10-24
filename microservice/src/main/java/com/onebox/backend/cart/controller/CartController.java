package com.onebox.backend.cart.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onebox.backend.cart.model.Cart;
import com.onebox.backend.cart.model.Product;
import com.onebox.backend.cart.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Cart REST API", description = "This API serves all functionality for managing shopping carts")

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
    @Operation(summary = "Create a new cart", description = "Creates a new empty cart and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Retrieve a cart by ID", description = "Returns the cart with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCardById(@PathVariable String id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    /**
     * Retrieves all carts.
     *
     * @return HTTP response with the list of all carts
     */
    @Operation(summary = "Retrieve all carts", description = "Returns a list of all carts stored in the repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Update a cart by adding a product o products", description = "Updates the cart with the specified ID by adding products to it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/{id}/products")
    public ResponseEntity<Cart> updateCart(@PathVariable String id,
            @RequestBody List<Product> products) {
        return ResponseEntity.ok(cartService.addProductsToCart(id, products));
    }

    /**
     * Deletes a cart by its ID.
     *
     * @param id the ID of the cart to delete
     * @return HTTP response with no content
     */
    @Operation(summary = "Delete a cart by ID", description = "Deletes the cart with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

}
