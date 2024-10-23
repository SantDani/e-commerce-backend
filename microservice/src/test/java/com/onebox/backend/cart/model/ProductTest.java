package com.onebox.backend.cart.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductTest {

    private Product product1;
    private Product product2;
    private Product productWithNullId;

    @BeforeEach
    public void setup() {
        product1 = new Product(1, "Product1", 10);
        product2 = new Product(2, "Product2", 5);
        productWithNullId = new Product(null, "Product3", 3);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(product1.equals(product1));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(product1.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        String someObject = "Some object";
        assertFalse(product1.equals(someObject));
    }

    @Test
    public void testEqualsSameId() {
        Product productWithSameId = new Product(1, "ProductDifferent", 20);
        assertTrue(product1.equals(productWithSameId));
    }

    @Test
    public void testEqualsDifferentId() {
        assertFalse(product1.equals(product2));
    }

    @Test
    public void testEqualsOneNullId() {
        assertFalse(productWithNullId.equals(product1));
    }

    @Test
    public void testEqualsBothNullId() {
        Product anotherProductWithNullId = new Product(null, "ProductAnother", 7);
        assertFalse(productWithNullId.equals(anotherProductWithNullId));
    }

    @Test
    public void testHashCodeSameId() {
        Product productWithSameId = new Product(1, "ProductDifferent", 20);
        assertEquals(product1.hashCode(), productWithSameId.hashCode());
    }

    @Test
    public void testHashCodeDifferentId() {
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    public void testHashCodeNullId() {
        assertEquals(0, productWithNullId.hashCode());
    }
}
