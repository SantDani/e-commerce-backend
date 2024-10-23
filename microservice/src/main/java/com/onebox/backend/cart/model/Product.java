package com.onebox.backend.cart.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Product {

    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The description parameter is alphanumeric, therefore, only letters and numbers are allowed. No special characters or spaces are allowed.")
    private String description;
    private int amount;

    public Product(Integer id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)// same object
            return true;
        if (o == null || getClass() != o.getClass()) // same class
            return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
