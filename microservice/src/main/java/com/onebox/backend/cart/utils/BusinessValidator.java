package com.onebox.backend.cart.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ErrorCode;
import com.onebox.backend.cart.exception.model.ProductException;
import com.onebox.backend.cart.model.Product;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * BusinessValidator is a utility class that provides validation methods for
 * business entities.
 */
@Component
public class BusinessValidator {

    private final Validator validator;

    public BusinessValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Validates the given Product object. If there are any validation errors, a
     * ProductException is thrown with the appropriate error message and code.
     * 
     * @param product the Product object to validate
     * @throws ProductException if there are validation errors
     */
    public void validateProduct(Product product) {
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> "Validation error in ProductId: " + product.getId() + " with property: ' "
                            + violation.getPropertyPath() + "' and value: '" + violation.getInvalidValue()
                            + "' because " + violation.getMessage())
                    .collect(Collectors.joining("\n"));

            ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
            throw new ProductException(errorCode.getCode(), errorCode.getMessage() + " " + errorMessage,
                    errorCode.getHttpStatus());
        }
    }

    /**
     * Validates the ID of a cart. If the ID is null or empty, a CartException is
     * thrown with the appropriate error message and code.
     * 
     * @param id the ID to validate
     * @throws CartException if the ID is null or empty
     */
    public void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            ErrorCode nullEmpty = ErrorCode.CART_NOT_FOUND;
            throw new CartException(nullEmpty.getCode(),
                    nullEmpty.getMessage(),
                    nullEmpty.getHttpStatus());
        }
    }

}
