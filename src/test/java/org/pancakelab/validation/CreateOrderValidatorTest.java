package org.pancakelab.validation;

import org.junit.jupiter.api.Test;
import org.pancakelab.model.Order;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderValidatorTest {

    CreateOrderValidator validator = new CreateOrderValidator();

    @Test
    void GivenOrder_WhenInvalid_ThenValidationFails() {
        Order order = new Order(-1, -2);
        ValidationResult result = validator.validate(order);
        assertTrue(result.isInvalid());
        assertEquals(2, result.getErrors().size());

        order = new Order(-1, 2);
        result = validator.validate(order);
        assertTrue(result.isInvalid());
        assertEquals(1, result.getErrors().size());

        order = new Order(1, -2);
        result = validator.validate(order);
        assertTrue(result.isInvalid());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    void GivenOrder_WhenValid_ThenValidationSucceed() {
        Order order = new Order(1, 2);
        ValidationResult result = validator.validate(order);
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }


}