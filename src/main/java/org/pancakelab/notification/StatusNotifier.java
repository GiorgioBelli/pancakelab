package org.pancakelab.notification;

import org.pancakelab.model.Order;
import org.pancakelab.notification.messages.StatusUpdate;

public class StatusNotifier extends Notifier<StatusUpdate> {

    protected void notify(StatusUpdate update) {
        super.notify(update, update.getOrder().getState().getStatus().toString());
    }

    public void notifyCreatedOrder(Order order) {
        StatusUpdate message = new StatusUpdate(order);
        this.notify(message);
    }

    public void notifyCompletedOrder(Order order) {
        StatusUpdate message = new StatusUpdate(order);
        this.notify(message);
    }

    public void notifyPreparedOrder(Order order) {
        StatusUpdate message = new StatusUpdate(order);
        this.notify(message);
    }

    public void notifyDeliveredOrder(Order order) {
        StatusUpdate message = new StatusUpdate(order);
        this.notify(message);
    }

}
