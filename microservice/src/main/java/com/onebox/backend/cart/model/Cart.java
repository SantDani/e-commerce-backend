package com.onebox.backend.cart.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public class Cart {

    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1", description = "The unique ID of the cart")
    private String id;
    @Schema(name = "lastAccessed", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2023-10-01T12:00:00", description = "The last accessed time of the cart")
    private LocalDateTime lastAccessed;
    @Schema(name = "products", requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "The set of products in the cart")
    private Set<Product> products;

    public Cart() {
        this.id = UUID.randomUUID().toString();
        this.lastAccessed = LocalDateTime.now();
        this.products = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public void setId(String id) {
        this.id = id;
    }
}
