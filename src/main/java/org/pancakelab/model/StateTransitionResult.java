package org.pancakelab.model;

public interface StateTransitionResult {
    boolean isValid();
    default boolean isInvalid() {
        return !isValid();
    }
}
