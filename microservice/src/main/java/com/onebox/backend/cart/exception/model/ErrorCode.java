package com.onebox.backend.cart.exception.model;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    CART_NOT_FOUND("ERR001", "Cart not found.", HttpStatus.NOT_FOUND),
    AMOUNT_NEGATIVE("ERR002", "Amount cannot be negative", HttpStatus.BAD_REQUEST),
    OPERATION_FAILED("ERR003", "Operation failed", HttpStatus.NOT_FOUND),
    Cart_NULL_EMPTY("ERR004", "Cart or Cart ID cannot be null", HttpStatus.NOT_FOUND),
    Product_NULL_EMPTY("ERR005", "Product or Product ID cannot be null", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("ERR006", "Validation error", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
