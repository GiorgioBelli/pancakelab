package org.pancakelab.model.state;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderState;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.StateTransitionResult;

public class CompletedState implements OrderState {

    @Override
    public StateTransitionResult prepare(Order order) {
        order.setState(new PreparedState());
        return new ValidStateTransitionResult();
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.COMPLETED;
    }
}