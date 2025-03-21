package org.pancakelab.model;

import org.pancakelab.model.state.InvalidStateTransitionResult;

public interface OrderState {
    default StateTransitionResult cancel(Order order) {
        return new InvalidStateTransitionResult();
    }
    default StateTransitionResult complete(Order order) {
        return new InvalidStateTransitionResult();
    }
    default StateTransitionResult prepare(Order order) {
        return new InvalidStateTransitionResult();
    }
    default StateTransitionResult deliver(Order order) {
        return new InvalidStateTransitionResult();
    }
    OrderStatus getStatus();
}
