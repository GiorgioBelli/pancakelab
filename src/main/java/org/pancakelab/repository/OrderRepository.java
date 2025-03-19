package org.pancakelab.repository;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository {
    private final Map<UUID, Order> orders = new ConcurrentHashMap<>();
    private final Set<UUID> completedOrders = ConcurrentHashMap.newKeySet();
    private final Set<UUID> preparedOrders = ConcurrentHashMap.newKeySet();

    public List<Order> getOrders() {
        return orders.values().stream().toList();
    }

    public Set<UUID> getCompletedOrders() {
        return completedOrders;
    }

    public Set<UUID> getPreparedOrders() {
        return preparedOrders;
    }

    public Order getOrderById(UUID orderId) {
        return orders.get(orderId);
    }

    public void upsertOrder(Order order) {
        this.orders.put(order.getId(), order);
    }

    public Order delete(UUID orderId) {
        this.preparedOrders.remove(orderId);
        this.completedOrders.remove(orderId);
        return this.orders.remove(orderId);
    }

    public void updateStatus(UUID orderId, OrderStatus status) {
        switch (status) {
            case COMPLETED -> this.completedOrder(orderId);
            case PREPARED -> this.preparedOrder(orderId);
            case DELIVERED -> this.delete(orderId);
            case CREATED -> {}
        }
    }

    private void completedOrder(UUID orderId) {
        this.completedOrders.add(orderId);
        this.preparedOrders.remove(orderId);
    }

    private void preparedOrder(UUID orderId) {
        this.preparedOrders.add(orderId);
        this.completedOrders.remove(orderId);
    }

    public boolean hasStatus(UUID orderId, OrderStatus status) {
        if (Objects.isNull(status)) {
            return false;
        }
        return status.equals(getStatus(orderId));
    }

    public OrderStatus getStatus(UUID orderId) {
        if ( this.completedOrders.contains(orderId)) { return OrderStatus.COMPLETED; }
        if ( this.preparedOrders.contains(orderId)) { return OrderStatus.PREPARED; }
        if ( this.orders.containsKey(orderId)) { return OrderStatus.CREATED; }
        else { return OrderStatus.DELIVERED; }
    }

}
