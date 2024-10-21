package com.onebox.backend.cart.model;

import lombok.Data;

@Data
public class Product {

    private Integer id;
    // TODO: handle alphanumeric param
    private String description;
    private int amount;

    public Product(Integer id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }
}
