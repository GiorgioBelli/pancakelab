package org.pancakelab.validation;

import org.pancakelab.model.OrderStatus;

public class OrderStatusValidator {

    public ValidationResult validateNextState(OrderStatus currentStatus, OrderStatus nextStatus) {
        ValidationResult validationResult = ValidationResult.valid();
        if (currentStatus.canChangeTo(nextStatus)) {
            ValidationResult.invalid(String.format("Order with state %s cannot be move to %s", currentStatus, nextStatus));
        }
        return validationResult;
    }

    public ValidationResult validateCancellation(OrderStatus currentStatus) {
        ValidationResult validationResult = ValidationResult.valid();
        if (currentStatus.canBeCanceled()) {
            ValidationResult.invalid(String.format("Order with state %s cannot be canceled", currentStatus));
        }
        return validationResult;
    }
}
