package com.onebox.backend.cart.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class Cart {

    private String id;
    private LocalDateTime lastAccessed;
    private Set<Product> products;

    public Cart() {
        this.id = UUID.randomUUID().toString();
        this.lastAccessed = LocalDateTime.now();
        this.products = new HashSet<>();
    }
}
