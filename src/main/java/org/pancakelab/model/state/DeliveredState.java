package org.pancakelab.model.state;

import org.pancakelab.model.OrderState;
import org.pancakelab.model.OrderStatus;

public class DeliveredState implements OrderState {
    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DELIVERED;
    }
}