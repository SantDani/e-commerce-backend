package com.onebox.backend.cart.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ErrorCode;
import com.onebox.backend.cart.model.Cart;

@Repository
public class CartRepository {

    private Map<String, Cart> cartStore = new ConcurrentHashMap<>();

    /**
     * Saves the cart object in the cartStore.
     * 
     * @param cart The cart object to save.
     * @return The saved cart.
     * @throws CartException if the cart is null or has a null ID.
     */
    public Cart save(Cart cart) {

        if (cart == null || cart.getId() == null) {
            return null;
        }

        cartStore.put(cart.getId(), cart);

        return cart;
    }

    /**
     * Finds a cart by its ID.
     * 
     * @param id The ID of the cart to find.
     * @return The cart object, if found.
     * @throws CartException if the ID is null, empty, or if no cart is found for
     *                       the given ID.
     */
    public Cart findById(String id) {

        if (id == null || id.isEmpty()) {
            return null;
        }

        return cartStore.get(id);
    }

    /**
     * Deletes a cart by its ID.
     * 
     * @param id The ID of the cart to delete.
     * @throws CartException if the ID is null, empty, or if no cart is found for
     *                       the given ID.
     */
    public void deleteById(String id) {
        if (id == null && cartStore.containsKey(id)) {
            cartStore.remove(id);
        } else {
            ErrorCode invalidIdList = ErrorCode.CART_NULL_EMPTY;
            throw new CartException(invalidIdList.getCode(),
                    invalidIdList.getMessage(),
                    invalidIdList.getHttpStatus());
        }
    }

    /**
     * Retrieves all carts from the cartStore.
     * 
     * @return A list of all cart objects.
     */
    public List<Cart> findAll() {
        return cartStore.values().stream().collect(Collectors.toList());
    }

    /**
     * Deletes multiple carts by their IDs.
     * 
     * @param ids The list of IDs of the carts to delete.
     * @throws CartException if the list of IDs is null or empty.
     */
    public void deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            ids.forEach(this::deleteById);
        } else {
            ErrorCode invalidIdList = ErrorCode.CART_NULL_EMPTY;
            throw new CartException(invalidIdList.getCode(),
                    invalidIdList.getMessage(),
                    invalidIdList.getHttpStatus());
        }

    }

}
