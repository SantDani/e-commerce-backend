package com.onebox.backend.cart.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.onebox.backend.cart.exception.model.ApiExceptionResponse;
import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ProductException;

@RestControllerAdvice
public class ApiExceptionHandle {

    Logger logger = LoggerFactory.getLogger(ApiExceptionHandle.class);

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ApiExceptionResponse> handleCartException(CartException ex) {
        String message = "Problems interact with Cart: CartException ->";
        logger.error(message, ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                message, ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ApiExceptionResponse> handleProductException(ProductException ex) {
        String message = "Problems interact with Product: ProductException -> ";

        logger.error(message, ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                message, ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
}
