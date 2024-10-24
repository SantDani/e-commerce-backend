package com.onebox.backend.cart.exception.model;

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

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}
