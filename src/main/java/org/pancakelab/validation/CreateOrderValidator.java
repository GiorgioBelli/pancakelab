package org.pancakelab.validation;

import org.pancakelab.model.Order;

public class CreateOrderValidator implements Validator {

    @Override
    public ValidationResult validate(Order order) {
        ValidationResult validationResult = ValidationResult.valid();
        if (order.getBuilding() <= 0) {
            validationResult.addError("Building number must be a positive number greater than 0");
        }
        if (order.getRoom() <=0 ) {
            validationResult.addError("Room number must be a positive number greater than 0");
        }
        return validationResult;
    }
}
