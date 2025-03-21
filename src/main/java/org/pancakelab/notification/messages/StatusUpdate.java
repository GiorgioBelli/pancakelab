package org.pancakelab.notification.messages;

import org.pancakelab.model.Order;

public class StatusUpdate extends OrderUpdate {
    public StatusUpdate(Order order) {
        super(order);
    }
}
