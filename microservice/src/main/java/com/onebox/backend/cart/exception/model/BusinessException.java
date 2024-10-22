package com.onebox.backend.cart.exception.model;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private String code;
    private HttpStatus httpStatus;

    public BusinessException(String code, String titleMessage, HttpStatus httpStatus) {
        super(titleMessage);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code='" + code + '\'' +
                ", message='" + getMessage() + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }

}
