package com.onebox.backend.cart.exception.model;

import org.springframework.http.HttpStatus;

public class ProductException extends BusinessException {

    public ProductException(String code, String message, HttpStatus httpStatus) {
        super(code, message, httpStatus);
    }

}
