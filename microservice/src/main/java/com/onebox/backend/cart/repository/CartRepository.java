package com.onebox.backend.cart.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.onebox.backend.cart.model.Cart;

@Repository
public class CartRepository {

    private Map<String, Cart> cartStore = new ConcurrentHashMap<>();

    public Cart save(Cart cart) {

        // TODO: if cart is null or getCartId is null, throw custom exception

        cartStore.put(cart.getId(), cart);

        return cart;
    }

    public Cart findById(String id) {
        return cartStore.get(id);
    }

    public void deleteById(String id) {
        cartStore.remove(id);
    }

    public List<Cart> findAll() {
        return cartStore.values().stream().collect(Collectors.toList());
    }

    public void deleteByIds(List<String> ids) {
        ids.forEach(cartStore::remove);
    }

}
