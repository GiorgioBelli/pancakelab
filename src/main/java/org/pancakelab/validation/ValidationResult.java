package org.pancakelab.validation;

import java.util.*;

public class ValidationResult {

    private final Collection<String> errors;

    private ValidationResult(Collection<String> errors) {
        this.errors = errors;
    }

    static ValidationResult valid(){
        return new ValidationResult(new LinkedList<>());
    }

    static ValidationResult invalid(String ...errors){
        return new ValidationResult(Arrays.asList(errors));
    }

    public boolean isValid() {
        return this.errors.isEmpty();
    }

    public boolean isInvalid() {
        return !isValid();
    }

    public ValidationResult addError(String error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public String toString() {
        return "["+ String.join(", ", this.errors) +"]";
    }
}
