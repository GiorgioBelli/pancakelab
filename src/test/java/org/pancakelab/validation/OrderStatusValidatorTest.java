package org.pancakelab.validation;

import org.junit.jupiter.api.Test;
import org.pancakelab.model.OrderStatus;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStatusValidatorTest {

    OrderStatusValidator validator = new OrderStatusValidator();

    @Test
    void GivenTwoStates_WhenSubsequent_ThenValidationSucceed() {
        OrderStatus[] statuses = OrderStatus.values();
        for (int i = 0; i < statuses.length-1; i++) {
            ValidationResult result = validator.validateNextState(statuses[i], statuses[i+1]);
            assertTrue(result.isValid());
        }
    }

    @Test
    void validateCancellation() {
        OrderStatus[] statuses = OrderStatus.values();

        //cancellable
        assertTrue(validator.validateCancellation(OrderStatus.CREATED).isValid());

        // not cancellable
        for (int i = 1; i < statuses.length; i++) {
            ValidationResult result = validator.validateCancellation(statuses[i]);
            assertTrue(result.isInvalid(), String.format("Invalid cancellable state: %s", statuses[i]));
        }
    }
}