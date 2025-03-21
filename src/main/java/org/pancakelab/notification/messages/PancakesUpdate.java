package org.pancakelab.notification.messages;

import org.pancakelab.model.Order;

public class PancakesUpdate extends OrderUpdate {

    private final PancakeUpdateAction action;
    private final String description;
    private final int count;

    public PancakesUpdate(Order order, PancakeUpdateAction action, String description, int count) {
        super(order);
        this.action = action;
        this.description = description;
        this.count = count;
    }

    public PancakeUpdateAction getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }
}
