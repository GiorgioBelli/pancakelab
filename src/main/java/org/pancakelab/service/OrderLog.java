package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.notification.Subscriber;
import org.pancakelab.notification.messages.OrderUpdate;
import org.pancakelab.notification.messages.PancakeUpdateAction;
import org.pancakelab.notification.messages.PancakesUpdate;
import org.pancakelab.notification.messages.StatusUpdate;

import java.util.List;

public class OrderLog implements Subscriber<OrderUpdate> {
    private static final StringBuilder log = new StringBuilder();

    public synchronized static void logAddPancake(Order order, String description) {
        log.append("Added pancake with description '%s' ".formatted(description))
           .append("to order %s containing %d pancakes, ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized static void logRemovePancakes(Order order, String description, int count) {
        log.append("Removed %d pancake(s) with description '%s' ".formatted(count, description))
           .append("from order %s now containing %d pancakes, ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized static void logCancelOrder(Order order) {
        log.append("Cancelled order %s with %d pancakes ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized static void logDeliverOrder(Order order) {
        log.append("Order %s with %d pancakes ".formatted(order.getId(), order.getPancakes().size()))
           .append("for building %d, room %d out for delivery.".formatted(order.getBuilding(), order.getRoom()));
    }

    public synchronized static void print() {
        System.out.println(log);
    }

    @Override
    public void update(OrderUpdate orderUpdate) {
        if (orderUpdate instanceof StatusUpdate) {
            this.statusUpdate((StatusUpdate) orderUpdate);
        }
        else if (orderUpdate instanceof PancakesUpdate) {
            this.pancakesUpdate((PancakesUpdate) orderUpdate);
        }
    }

    protected void statusUpdate(StatusUpdate statusUpdate) {
        switch (statusUpdate.getOrder().getState().getStatus()){
            case CANCELED -> logCancelOrder(statusUpdate.getOrder());
            case DELIVERED -> logDeliverOrder(statusUpdate.getOrder());
        }
    }

    protected void pancakesUpdate(PancakesUpdate pancakesUpdate) {
        Order order = pancakesUpdate.getOrder();
        switch (pancakesUpdate.getAction()) {
            case ADD -> logAddPancake(order, pancakesUpdate.getDescription());
            case REMOVE -> logRemovePancakes(order, pancakesUpdate.getDescription(), pancakesUpdate.getCount());
        }
    }

    @Override
    public List<String> getSubject() {
        return List.of(OrderStatus.DELIVERED.toString(),
                OrderStatus.CANCELED.toString(),
                PancakeUpdateAction.ADD.toString(),
                PancakeUpdateAction.REMOVE.toString());
    }
}
