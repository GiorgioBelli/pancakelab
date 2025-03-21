package org.pancakelab.model.state;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderState;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.StateTransitionResult;

public class PreparedState implements OrderState {
    @Override
    public StateTransitionResult deliver(Order order) {
        order.setState(new DeliveredState());
        return new ValidStateTransitionResult();
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PREPARED;
    }
}