package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderActionResult;
import org.pancakelab.model.pancakes.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
    private final Map<UUID, Order>    orders          = new ConcurrentHashMap<>();
    private final Set<UUID>           completedOrders = new HashSet<>();
    private final Set<UUID>           preparedOrders  = new HashSet<>();

    public Order createOrder(int building, int room) {
        Order order = new Order(building, room);
        orders.put(order.getId(), order);
        return order;
    }

    public List<String> viewOrder(UUID orderId) {
        Order order = orders.get(orderId);
        if (Objects.isNull(order)) {
            return List.of();
        }
        return order.view();
    }

    public OrderActionResult<Void> addPancake(UUID orderId, Pancake pancake, int count) {
        Order order = orders.get(orderId);
        if (Objects.isNull(orderId)) {
            return OrderActionResult.failed("Order not found");
        }
        for (int i = 0; i < count; i++) {
            order.addPancake(pancake);
        }
        OrderLog.logAddPancake(order, pancake.description());
        return OrderActionResult.success("Pancake added to order", null);
    }

    public OrderActionResult<Void> removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        Order order = orders.get(orderId);
        if (Objects.isNull(orderId)) {
            return OrderActionResult.failed("Order not found");
        }
        order.getPancakes().removeIf(pancake ->
                   pancake.description().equals(description) &&
                   removedCount.getAndIncrement() < count
        );
        OrderLog.logRemovePancakes(order, description, removedCount.get());
        return OrderActionResult.success("Pancake removed from order", null);
    }

    public OrderActionResult<Void> cancelOrder(UUID orderId) {
        Order order = orders.get(orderId);
        if (Objects.isNull(orderId)) {
            return OrderActionResult.failed("Order not found");
        }
        OrderLog.logCancelOrder(order);
        orders.remove(orderId);
        completedOrders.removeIf(u -> u.equals(orderId));
        preparedOrders.removeIf(u -> u.equals(orderId));

        OrderLog.logCancelOrder(order);
        return OrderActionResult.success("Order successfully removed", null);
    }

    public void completeOrder(UUID orderId) {
        completedOrders.add(orderId);
    }

    public Set<UUID> listCompletedOrders() {
        return completedOrders;
    }

    public void prepareOrder(UUID orderId) {
        preparedOrders.add(orderId);
        completedOrders.removeIf(u -> u.equals(orderId));
    }

    public Set<UUID> listPreparedOrders() {
        return preparedOrders;
    }

    public OrderActionResult<Object[]> deliverOrder(UUID orderId) {
        if (!preparedOrders.contains(orderId)) return null;

        Order order = orders.get(orderId);
        if (Objects.isNull(orderId)) {
            return OrderActionResult.failed("Order not found");
        }
        List<String> pancakesToDeliver = viewOrder(orderId);
        OrderLog.logDeliverOrder(order);

        orders.remove(orderId);
        preparedOrders.removeIf(u -> u.equals(orderId));

        return OrderActionResult.success("Order delivered", new Object[] {order, pancakesToDeliver});
    }
}
