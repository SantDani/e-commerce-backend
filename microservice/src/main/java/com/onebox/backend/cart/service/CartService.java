package com.onebox.backend.cart.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.onebox.backend.cart.model.Cart;
import com.onebox.backend.cart.model.Product;
import com.onebox.backend.cart.repository.CartRepository;

@Service
public class CartService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CartRepository cartRepository;

    Logger logger = LoggerFactory.getLogger(CartService.class);

    @Value("${cart.inactivity.minutes:10}")
    private int inactivityMinutes;

    @Value("${cart.cleanup.fixedRate:60000}")
    private String cleanupFixedRate;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    public Cart getCartById(String id) {

        // TODO: validate id

        Cart cart = cartRepository.findById(id);

        if (cart == null) {
            // TODO: Cart not found with ID
        }
        return cart;
    }

    public List<Cart> getAllsCarts() {
        return cartRepository.findAll();
    }

    public Cart addProductsToCart(String cartId, List<Product> products) {

        Cart cart = cartRepository.findById(cartId);

        if (cart == null) {
            // TODO: throw Cart not found with ID
        } else {

            for (Product product : products) {
                // TODO: validate Product
                if (product.getAmount() < 0) {
                    // TODO: throw Amount cannot be negative
                }

                if (cart.getProducts().contains(product)) {
                    Optional<Product> existingProduct = cart.getProducts().stream()
                            .filter(item -> item.equals(product))
                            .findFirst();
                    existingProduct.get().setAmount(existingProduct.get().getAmount() + product.getAmount());

                } else {
                    cart.getProducts().add(product);
                }

                cart.setLastAccessed(LocalDateTime.now());
            }

        }
        return cartRepository.save(cart);
    }

    public Cart deleteCart(String id) {
        Cart cart = getCartById(id);
        cartRepository.deleteById(id);
        return cart;
    }

    @Scheduled(fixedRateString = cleanupFixedRate)
    public void removeInactiveCarts() {

        // TODO: test this method
        LocalDateTime inactiveSince = LocalDateTime.now().minusMinutes(inactivityMinutes);
        String formattedDate = inactiveSince.format(formatter);

        logger.debug("Checking for inactive carts since: {} (inactivityMinutes: {})", formattedDate, inactivityMinutes);

        List<String> inactiveCartIds = cartRepository.findAll().stream()
                .filter(item -> {
                    logger.debug("Cart {} last accessed: {} < {}", item.getId(),
                            item.getLastAccessed().format(formatter), inactiveSince.format(formatter));

                    System.out.printf("Cart %s last accessed: %s < %s%n", item.getId(),
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
}
