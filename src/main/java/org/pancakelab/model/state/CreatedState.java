package org.pancakelab.model.state;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderState;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.StateTransitionResult;

public class CreatedState implements OrderState {

    @Override
    public StateTransitionResult cancel(Order order) {
        order.setState(new CanceledState());
        return new ValidStateTransitionResult();
    }

    @Override
    public StateTransitionResult complete(Order order) {
        order.setState(new CompletedState());
        return new ValidStateTransitionResult();
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CREATED;
    }

}