package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderActionResult;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.notification.OrderNotifier;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.validation.CreateOrderValidator;
import org.pancakelab.validation.OrderStatusValidator;
import org.pancakelab.validation.ValidationResult;

import java.util.*;

public class PancakeService {
    private final OrderRepository orderRepository;
    private final CreateOrderValidator createOrderValidator;
    private final OrderStatusValidator orderStatusValidator;
    private final OrderNotifier orderNotifier;

    public PancakeService() {
        this(new OrderRepository(), new CreateOrderValidator(), new OrderStatusValidator(), new OrderNotifier());
    }

    public PancakeService(OrderRepository orderRepository, CreateOrderValidator createOrderValidator, OrderStatusValidator orderStatusValidator, OrderNotifier orderNotifier) {
        this.orderRepository = orderRepository;
        this.createOrderValidator = createOrderValidator;
        this.orderStatusValidator = orderStatusValidator;
        this.orderNotifier = orderNotifier;
    }

    public OrderActionResult<Order> createOrder(int building, int room) {
        Order order = new Order(building, room);
        ValidationResult validationResult = createOrderValidator.validate(order);
        if (validationResult.isInvalid()) {
            return OrderActionResult.failed(String.format("Cannot create order due to the following errors: %s", validationResult));
        }
        orderRepository.upsertOrder(order);
        orderNotifier.notifyCreatedOrder(order);
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
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        order.removePancakesByDescription(description, count);
        OrderLog.logRemovePancakes(order, description, count);
        return OrderActionResult.success();
    }

    public OrderActionResult<Void> cancelOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        OrderStatus status = orderRepository.getStatus(orderId);
        ValidationResult validationResult = orderStatusValidator.validateCancellation(status);
        if(validationResult.isInvalid()) {
            return OrderActionResult.failed(validationResult.toString());
        }
        orderRepository.delete(orderId);
        OrderLog.logCancelOrder(order);
        return OrderActionResult.success();
    }

    public OrderActionResult<Void> completeOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        OrderStatus status = orderRepository.getStatus(order.getId());
        ValidationResult validationResult = orderStatusValidator.validateNextState(status, OrderStatus.COMPLETED);
        if(validationResult.isInvalid()) {
            return OrderActionResult.failed(validationResult.toString());
        }
        orderRepository.updateStatus(order.getId(), OrderStatus.COMPLETED);
        orderNotifier.notifyCompletedOrder(order);
        return OrderActionResult.success();
    }

    public Set<UUID> listCompletedOrders() {
        return orderRepository.getCompletedOrders();
    }

    public OrderActionResult<Void> prepareOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        OrderStatus status = orderRepository.getStatus(order.getId());
        ValidationResult validationResult = orderStatusValidator.validateNextState(status, OrderStatus.PREPARED);
        if(validationResult.isInvalid()) {
            return OrderActionResult.failed(validationResult.toString());
        }
        orderRepository.updateStatus(order.getId(), OrderStatus.PREPARED);
        orderNotifier.notifyPreparedOrder(order);
        return OrderActionResult.success();
    }

    public Set<UUID> listPreparedOrders() {
        return orderRepository.getPreparedOrders();
    }

    public OrderActionResult<Object[]> deliverOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        OrderStatus status = orderRepository.getStatus(order.getId());
        ValidationResult validationResult = orderStatusValidator.validateNextState(status, OrderStatus.DELIVERED);
        if(validationResult.isInvalid()) {
            return OrderActionResult.failed(validationResult.toString());
        }

        List<String> pancakesToDeliver = viewOrder(order.getId());
        OrderLog.logDeliverOrder(order);
        orderRepository.delete(orderId);
        orderNotifier.notifyDeliveredOrder(order);
        return OrderActionResult.success(new Object[]{order, pancakesToDeliver});
    }
}
