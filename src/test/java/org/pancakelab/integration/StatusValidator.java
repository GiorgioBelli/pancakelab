package org.pancakelab.integration;

import org.pancakelab.model.OrderStatus;
import org.pancakelab.notification.Subscriber;
import org.pancakelab.notification.messages.StatusUpdate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StatusValidator implements Subscriber<StatusUpdate> {

    public final Map<UUID, Set<OrderStatus>> orders;

    public StatusValidator() {
        this.orders = new ConcurrentHashMap<>();
    }

    @Override
    public void update(StatusUpdate update) {
        OrderStatus status = update.getOrder().getState().getStatus();
        if (OrderStatus.CREATED.equals(status)){
            // initialize new order with all the states
            orders.put(update.getOrder().getId(), createAllStateSet());
            return;
        }

        // remove the processed state
        orders.get(update.getOrder().getId()).remove(status);
    }

    @Override
    public List<String> getSubject() {
        return Arrays.stream(OrderStatus.values()).map(OrderStatus::toString).toList();
    }

    private Set<OrderStatus> createAllStateSet() {
        return Arrays.stream(OrderStatus.values())
                .filter(status -> !status.equals(OrderStatus.CREATED))
                .collect(Collectors.toSet());
    }

    public boolean isValid() {
        return orders.values().stream().anyMatch(set -> !set.isEmpty());
    }
}
