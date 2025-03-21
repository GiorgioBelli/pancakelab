package org.pancakelab.repository;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderRepository {
    private final Map<UUID, Order> orders = new ConcurrentHashMap<>();

    public List<Order> getOrders() {
        return orders.values().stream().toList();
    }

    public Set<UUID> getCompletedOrders() {
        return orders.values().stream()
                .filter(order -> OrderStatus.COMPLETED.equals(order.getState().getStatus()))
                .map(Order::getId)
                .collect(Collectors.toSet());
    }

    public Set<UUID> getPreparedOrders() {
        return orders.values().stream()
                .filter(order -> OrderStatus.PREPARED.equals(order.getState().getStatus()))
                .map(Order::getId)
                .collect(Collectors.toSet());
    }

    public Order getOrderById(UUID orderId) {
        return orders.get(orderId);
    }

    public void upsertOrder(Order order) {
        this.orders.put(order.getId(), order);
    }

    public Order delete(UUID orderId) {
        return this.orders.remove(orderId);
    }

}
