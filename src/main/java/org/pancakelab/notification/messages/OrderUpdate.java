package org.pancakelab.notification.messages;

import org.pancakelab.model.Order;

public abstract class OrderUpdate {

    protected final Order order;

    protected OrderUpdate(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
