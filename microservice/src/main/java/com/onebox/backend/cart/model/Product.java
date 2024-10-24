package com.onebox.backend.cart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public class Product {

    @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1", description = "The unique ID of the product")
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The description parameter is alphanumeric, therefore, only letters and numbers are allowed. No special characters or spaces are allowed.")
    @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED, example = "Product123", description = "The description of the product")
    private String description;
    @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED, example = "3", description = "The amount of the product")
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

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
