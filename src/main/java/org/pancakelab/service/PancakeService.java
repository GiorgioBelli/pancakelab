package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderActionResult;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.validation.CreateOrderValidator;
import org.pancakelab.validation.Validator;
import org.pancakelab.validation.ValidationResult;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
    private final OrderRepository orderRepository = new OrderRepository();
    private final Validator createOrderValidator = new CreateOrderValidator();

    public OrderActionResult<Order> createOrder(int building, int room) {
        Order order = new Order(building, room);
        ValidationResult validationResult = createOrderValidator.validate(order);
        if (validationResult.isInvalid()) {
            return OrderActionResult.failed(String.format("Cannot create order due to the following errors: %s", validationResult));
        }
        orderRepository.upsertOrder(order);
        return OrderActionResult.success(order);
    }

    public List<String> viewOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return List.of();
        }
        return order.view();
    }

    public OrderActionResult<Void> addPancake(UUID orderId, Pancake pancake, int count) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        for (int i = 0; i < count; i++) {
            order.addPancake(pancake);
        }
        OrderLog.logAddPancake(order, pancake.description());
        return OrderActionResult.success();
    }

    public OrderActionResult<Void> removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        order.getPancakes().removeIf(pancake -> pancake.description().equals(description) && removedCount.getAndIncrement() < count);
        OrderLog.logRemovePancakes(order, description, removedCount.get());
        return OrderActionResult.success();
    }

    public OrderActionResult<Void> cancelOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        orderRepository.delete(orderId);
        OrderLog.logCancelOrder(order);
        return OrderActionResult.success();
    }

    public void completeOrder(UUID orderId) {
        orderRepository.updateStatus(orderId, OrderStatus.COMPLETED);
    }

    public Set<UUID> listCompletedOrders() {
        return orderRepository.getCompletedOrders();
    }

    public void prepareOrder(UUID orderId) {
        orderRepository.updateStatus(orderId, OrderStatus.PREPARED);
    }

    public Set<UUID> listPreparedOrders() {
        return orderRepository.getPreparedOrders();
    }

    public OrderActionResult<Object[]> deliverOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        if (!orderRepository.hasStatus(order.getId(), OrderStatus.PREPARED)) {
            return null;
        }

        List<String> pancakesToDeliver = viewOrder(orderId);
        OrderLog.logDeliverOrder(order);
        orderRepository.delete(orderId);
        return OrderActionResult.success(new Object[]{order, pancakesToDeliver});
    }
}
