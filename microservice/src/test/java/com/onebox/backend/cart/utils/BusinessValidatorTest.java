package com.onebox.backend.cart.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import jakarta.validation.Path;
import com.onebox.backend.cart.exception.model.ProductException;
import com.onebox.backend.cart.model.Product;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BusinessValidatorTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private BusinessValidator businessValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateProductSuccess() {
        Product validProduct = new Product(1, "ValidProduct", 10);
        when(validator.validate(validProduct)).thenReturn(Collections.emptySet());
        assertDoesNotThrow(() -> businessValidator.validateProduct(validProduct));
    }

    @Test
    public void testValidateProductWithViolations() {
        Product invalidProduct = new Product(1, "InvalidProduct", -5);
        ConstraintViolation<Product> violation = createViolationMock("amount", invalidProduct.getAmount(),
                "must be positive");
        Set<ConstraintViolation<Product>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(invalidProduct)).thenReturn(violations);
        assertThrows(ProductException.class, () -> businessValidator.validateProduct(invalidProduct));
    }

    @Test
    public void testValidateProductWithMultipleViolations() {
        Product invalidProduct = new Product(1, "", -5);
        ConstraintViolation<Product> violation1 = createViolationMock("description", invalidProduct.getDescription(),
                "must not be empty");
        ConstraintViolation<Product> violation2 = createViolationMock("amount", invalidProduct.getAmount(),
                "must be positive");
        Set<ConstraintViolation<Product>> violations = new HashSet<>();
        violations.add(violation1);
        violations.add(violation2);
        when(validator.validate(invalidProduct)).thenReturn(violations);
        assertThrows(ProductException.class, () -> businessValidator.validateProduct(invalidProduct));
    }

    private <T> ConstraintViolation<T> createViolationMock(String property, Object invalidValue, String message) {
        @SuppressWarnings("unchecked")
        ConstraintViolation<T> violation = org.mockito.Mockito.mock(ConstraintViolation.class);
        Path propertyPath = Mockito.mock(Path.class);
        when(propertyPath.toString()).thenReturn(property);
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(violation.getInvalidValue()).thenReturn(invalidValue);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }

}
