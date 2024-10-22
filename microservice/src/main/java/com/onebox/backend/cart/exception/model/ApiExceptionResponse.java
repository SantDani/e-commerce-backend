package com.onebox.backend.cart.exception.model;

import lombok.Data;

@Data
public class ApiExceptionResponse {

    private String title;
    private String code;
    private String detail;

    public ApiExceptionResponse(String title, String code, String detail) {
        this.title = title;
        this.code = code;
        this.detail = detail;
    }

    public ApiExceptionResponse(String title, String code) {
        this.title = title;
        this.code = code;
        this.detail = null;
    }
}
