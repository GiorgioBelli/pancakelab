package org.pancakelab.notification;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;

public class OrderNotifier extends Notifier {

    public void notifyCreatedOrder(Order order) {
        OrderUpdate message = new OrderUpdate(order, OrderStatus.CREATED);
        this.notify(message);
    }

    public void notifyCompletedOrder(Order order) {
        OrderUpdate message = new OrderUpdate(order, OrderStatus.COMPLETED);
        this.notify(message);
    }

    public void notifyPreparedOrder(Order order) {
        OrderUpdate message = new OrderUpdate(order, OrderStatus.PREPARED);
        this.notify(message);
    }

    public void notifyDeliveredOrder(Order order) {
        OrderUpdate message = new OrderUpdate(order, OrderStatus.DELIVERED);
        this.notify(message);
    }

}
