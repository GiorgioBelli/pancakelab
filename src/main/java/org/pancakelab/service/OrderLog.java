package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.notification.messages.PancakesUpdate;
import org.pancakelab.notification.messages.StatusUpdate;

public class OrderLog {
    private final StringBuilder log = new StringBuilder();

    public synchronized void logAddPancake(Order order, String description) {
        log.append("Added pancake with description '%s' ".formatted(description))
           .append("to order %s containing %d pancakes, ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized void logRemovePancakes(Order order, String description, int count) {
        log.append("Removed %d pancake(s) with description '%s' ".formatted(count, description))
           .append("from order %s now containing %d pancakes, ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized void logCancelOrder(Order order) {
        log.append("Cancelled order %s with %d pancakes ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized void logDeliverOrder(Order order) {
        log.append("Order %s with %d pancakes ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d out for delivery.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized void print() {
        System.out.println(log);
    }

    public void statusUpdate(StatusUpdate statusUpdate) {
        switch (statusUpdate.getOrder().getState().getStatus()){
            case CANCELED -> logCancelOrder(statusUpdate.getOrder());
            case DELIVERED -> logDeliverOrder(statusUpdate.getOrder());
        }
    }

    public void pancakesUpdate(PancakesUpdate pancakesUpdate) {
        Order order = pancakesUpdate.getOrder();
        switch (pancakesUpdate.getAction()) {
            case ADD -> logAddPancake(order, pancakesUpdate.getDescription());
            case REMOVE -> logRemovePancakes(order, pancakesUpdate.getDescription(), pancakesUpdate.getCount());
        }
    }
}
