package org.pancakelab.notification;

import org.pancakelab.model.Order;
import org.pancakelab.notification.messages.PancakeUpdateAction;
import org.pancakelab.notification.messages.PancakesUpdate;

public class PancakesNotifier extends Notifier<PancakesUpdate> {

    protected void notify(PancakesUpdate update) {
        super.notify(update, update.getAction().toString());
    }

    public void notifyPancakeAdded(Order order, String description, int count) {
        PancakesUpdate message = new PancakesUpdate(order, PancakeUpdateAction.ADD, description, count);
        this.notify(message);
    }

    public void notifyPancakeRemoved(Order order, String description, int count) {
        PancakesUpdate message = new PancakesUpdate(order, PancakeUpdateAction.REMOVE, description, count);
        this.notify(message);
    }

}
