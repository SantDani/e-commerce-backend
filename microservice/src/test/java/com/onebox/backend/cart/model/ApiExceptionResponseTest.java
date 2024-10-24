package com.onebox.backend.cart.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.onebox.backend.cart.exception.model.ApiExceptionResponse;

public class ApiExceptionResponseTest {

    /**
     * Test constructor with all parameters.
     */
    @Test
    public void testConstructorWithAllParameters() {
        String title = "Error Title";
        String code = "ERROR_CODE_123";
        String detail = "Detailed error message";

        ApiExceptionResponse response = new ApiExceptionResponse(title, code, detail);

        assertEquals(title, response.getTitle());
        assertEquals(code, response.getCode());
        assertEquals(detail, response.getDetail());
    }

    /**
     * Test constructor with title and code only.
     */
    @Test
    public void testConstructorWithTitleAndCodeOnly() {
        String title = "Error Title";
        String code = "ERROR_CODE_123";

        ApiExceptionResponse response = new ApiExceptionResponse(title, code);

        assertEquals(title, response.getTitle());
        assertEquals(code, response.getCode());
        assertNull(response.getDetail()); // Ensure the detail is null
    }

}
