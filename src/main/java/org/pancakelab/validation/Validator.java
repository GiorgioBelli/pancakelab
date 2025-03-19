package org.pancakelab.validation;

import org.pancakelab.model.Order;

public interface Validator {

    ValidationResult validate(Order order);
}
