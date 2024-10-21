package com.onebox.backend.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.onebox.backend.cart.service.CartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public String createCart(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

    @GetMapping("/{id}")
    public String getCardById(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/all")
    public String getAllCarts(@RequestParam String param) {
        return new String();
    }

    @PutMapping("/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        // TODO: process PUT request

        return entity;
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable String id) {

    }

}
