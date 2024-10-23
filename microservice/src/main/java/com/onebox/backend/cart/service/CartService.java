package com.onebox.backend.cart.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ErrorCode;
import com.onebox.backend.cart.model.Cart;
import com.onebox.backend.cart.model.Product;
import com.onebox.backend.cart.repository.CartRepository;
import com.onebox.backend.cart.utils.BusinessValidator;

/**
 * Service class for managing shopping carts.
 */
@Service
public class CartService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CartRepository cartRepository;

    public BusinessValidator validator;

    Logger logger = LoggerFactory.getLogger(CartService.class);

    @Value("${cart.inactivity.minutes:10}")
    private int inactivityMinutes;

    /**
     * Constructor CartRepository and BusinessValidator.
     * 
     * @param cartRepository the repository to store carts
     * @param validator      the validator to validate business entities
     */
    public CartService(CartRepository cartRepository, BusinessValidator validator) {
        this.cartRepository = cartRepository;
        this.validator = validator;
    }

    /**
     * Creates a new empty cart.
     * 
     * @return the newly created cart saved in the repository.
     */
    public Cart createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    /**
     * Retrieves a cart by its ID.
     *
     * @param id the ID of the cart to retrieve
     * @return the cart with the specified ID
     * @throws CartException if no cart is found with the specified ID
     */
    public Cart getCartById(String id) {

        validateId(id);
        Cart cart = cartRepository.findById(id);

        if (cart == null) {
            ErrorCode cartNotFound = ErrorCode.CART_NOT_FOUND;
            throw new CartException(cartNotFound.getCode(),
                    cartNotFound.getMessage() + " ID: " + id,
                    cartNotFound.getHttpStatus());
        }
        return cart;
    }

    /**
     * Retrieves all carts stored in the repository.
     * 
     * @return list of all carts
     */
    public List<Cart> getAllsCarts() {
        return cartRepository.findAll();
    }

    /**
     * Adds a list of products to the cart with the given ID.
     * 
     * @param cartId   the ID of the cart
     * @param products the list of products to add
     * @return the updated cart
     */
    public Cart addProductsToCart(String cartId, List<Product> products) {

        validateId(cartId);

        if (products == null || products.isEmpty()) {
            ErrorCode invalidProductList = ErrorCode.PRODUCT_NULL_EMPTY;
            throw new CartException(invalidProductList.getCode(),
                    invalidProductList.getMessage(),
                    invalidProductList.getHttpStatus());
        }

        Cart cart = cartRepository.findById(cartId);

        if (cart == null) {
            ErrorCode cartNotFound = ErrorCode.CART_NOT_FOUND;
            throw new CartException(cartNotFound.getCode(),
                    cartNotFound.getMessage() + " ID: " + cartId,
                    cartNotFound.getHttpStatus());
        } else {

            for (Product product : products) {
                validator.validateProduct(product);
                if (product.getAmount() < 0) {
                    ErrorCode amountNegative = ErrorCode.AMOUNT_NEGATIVE;
                    throw new CartException(amountNegative.getCode(),
                            amountNegative.getMessage() + " ID: " + cartId,
                            amountNegative.getHttpStatus());
                }
                Optional<Product> existingProduct = cart.getProducts().stream()
                        .filter(item -> item.equals(product))
                        .findFirst();
                if (existingProduct.isPresent()) {
                    existingProduct.get().setAmount(existingProduct.get().getAmount() + product.getAmount());

                } else {
                    cart.getProducts().add(product);
                }

                cart.setLastAccessed(LocalDateTime.now());
            }

        }
        return cartRepository.save(cart);
    }

    /**
     * Deletes a cart by its ID.
     * 
     * @param id the ID of the cart to be deleted
     * @return the deleted cart
     */
    public Cart deleteCart(String id) {
        Cart cart = getCartById(id);

        cartRepository.deleteById(id);
        return cart;
    }

    /**
     * Scheduled task to remove inactive carts.
     */
    @Scheduled(fixedRateString = "${cart.cleanup.fixedRate:60000}")
    public void removeInactiveCarts() {

        // TODO: test this method
        LocalDateTime inactiveSince = LocalDateTime.now().minusMinutes(inactivityMinutes);
        String formattedDate = inactiveSince.format(formatter);

        logger.debug("Checking for inactive carts since: {} (inactivityMinutes: {})", formattedDate, inactivityMinutes);

        List<String> inactiveCartIds = cartRepository.findAll().stream()
                .filter(item -> {
                    logger.debug("Cart {} last accessed: {} < {}", item.getId(),
                            item.getLastAccessed().format(formatter), inactiveSince.format(formatter));
                    return item.getLastAccessed().isBefore(inactiveSince);
                })
                .map(Cart::getId)
                .collect(Collectors.toList());

        int numberOfInactiveCarts = inactiveCartIds.size();
        if (numberOfInactiveCarts > 0) {
            cartRepository.deleteByIds(inactiveCartIds);
            logger.info("Removed {} inactive carts", numberOfInactiveCarts);
        } else {
            logger.debug("No inactive carts found to remove");
        }
    }

    /**
     * Validates the ID of a cart. If the ID is null or empty, a CartException is
     * thrown with the appropriate error message and code.
     * 
     * @param id the ID to validate
     * @throws CartException if the ID is null or empty
     */
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            ErrorCode nullEmpty = ErrorCode.CART_NOT_FOUND;
            throw new CartException(nullEmpty.getCode(),
                    nullEmpty.getMessage(),
                    nullEmpty.getHttpStatus());
        }
    }
}
