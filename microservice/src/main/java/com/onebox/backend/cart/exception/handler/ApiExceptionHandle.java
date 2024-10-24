package com.onebox.backend.cart.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.onebox.backend.cart.exception.model.ApiExceptionResponse;
import com.onebox.backend.cart.exception.model.CartException;
import com.onebox.backend.cart.exception.model.ErrorCode;
import com.onebox.backend.cart.exception.model.ProductException;

/**
 * Global custom exception handler for handling exceptions thrown by the
 * app.
 * 
 */
@RestControllerAdvice
public class ApiExceptionHandle {

    Logger logger = LoggerFactory.getLogger(ApiExceptionHandle.class);

    /**
     * Handles CartException and returns a ResponseEntity with an
     * ApiExceptionResponse.
     *
     * @param ex the CartException thrown by the application
     * @return a ResponseEntity containing the ApiExceptionResponse and the
     *         appropriate HTTP status
     */
    @ExceptionHandler(CartException.class)
    public ResponseEntity<ApiExceptionResponse> handleCartException(CartException ex) {
        String message = "Problems interact with Cart: CartException ->";
        logger.error(message, ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                message, ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    /**
     * Handles ProductException and returns a ResponseEntity with an
     * ApiExceptionResponse.
     *
     * @param ex the ProductException thrown by the application
     * @return a ResponseEntity containing the ApiExceptionResponse and the
     *         appropriate HTTP status
     */
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ApiExceptionResponse> handleProductException(ProductException ex) {
        String message = "Problems interact with Product: ProductException -> ";

        logger.error(message, ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                message, ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    /**
     * Handles generic Exception and returns a ResponseEntity with an
     * ApiExceptionResponse.
     *
     * @param ex the Exception thrown by the application
     * @return a ResponseEntity containing the ApiExceptionResponse and the
     *         appropriate HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception ex) {

        ErrorCode error = ErrorCode.INTERNAL_SERVER_ERROR;
        logger.error("An unexpected error occurred: ", ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                error.getMessage(), error.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
