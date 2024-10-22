package com.onebox.backend.cart.exception.model;

import org.springframework.http.HttpStatus;

public class CartException extends BusinessException {

    public CartException(String code, String message, HttpStatus httpStatus) {
        super(code, message, httpStatus);
    }
}
