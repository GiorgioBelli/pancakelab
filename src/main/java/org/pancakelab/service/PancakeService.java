package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderActionResult;
import org.pancakelab.model.StateTransitionResult;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.notification.PancakesNotifier;
import org.pancakelab.notification.StatusNotifier;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.validation.CreateOrderValidator;
import org.pancakelab.validation.ValidationResult;

import java.util.*;

public class PancakeService {
    private final OrderRepository orderRepository;
    private final CreateOrderValidator createOrderValidator;
    private final StatusNotifier statusNotifier;
    private final PancakesNotifier pancakesNotifier;

    public PancakeService() {
        this(new OrderRepository(), new CreateOrderValidator(), new StatusNotifier(), new PancakesNotifier());
    }

    public PancakeService(OrderRepository orderRepository, CreateOrderValidator createOrderValidator, StatusNotifier statusNotifier, PancakesNotifier pancakesNotifier) {
        this.orderRepository = orderRepository;
        this.createOrderValidator = createOrderValidator;
        this.statusNotifier = statusNotifier;
        this.pancakesNotifier = pancakesNotifier;
    }

    public OrderActionResult<Order> createOrder(int building, int room) {
        Order order = new Order(building, room);
        ValidationResult validationResult = createOrderValidator.validate(order);
        if (validationResult.isInvalid()) {
            return OrderActionResult.failed(String.format("Cannot create order due to the following errors: %s", validationResult));
        }
        orderRepository.upsertOrder(order);
        statusNotifier.notifyCreatedOrder(order);
        return OrderActionResult.success(order);
    }

    public List<String> viewOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return List.of();
        }
        return order.view();
    }

    public OrderActionResult<Void> addPancake(UUID orderId, PancakeRecipe pancake, int count) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        synchronized (order) {
            for (int i = 0; i < count; i++) {
                order.addPancake(pancake);
            }
            OrderLog.logAddPancake(order, pancake.description());
            pancakesNotifier.notifyPancakeAdded(order, pancake.description(), count);
            return OrderActionResult.success();
        }
    }

    public OrderActionResult<Void> removePancakes(String description, UUID orderId, int count) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        synchronized (order) {
            order.removePancakesByDescription(description, count);
            OrderLog.logRemovePancakes(order, description, count);
            pancakesNotifier.notifyPancakeRemoved(order, description, count);
            return OrderActionResult.success();
        }
    }

    public OrderActionResult<Void> cancelOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        StateTransitionResult transitionResult = order.cancel();
        if(transitionResult.isInvalid()) {
            return OrderActionResult.failed("Cannot cancel order");
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
        synchronized (order) {
            StateTransitionResult transitionResult = order.complete();
            if(transitionResult.isInvalid()) {
                return OrderActionResult.failed("Cannot complete order");
            }
            statusNotifier.notifyCompletedOrder(order);
            return OrderActionResult.success();
        }
    }

    public Set<UUID> listCompletedOrders() {
        return orderRepository.getCompletedOrders();
    }

    public OrderActionResult<Void> prepareOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        synchronized (order) {
            StateTransitionResult transitionResult = order.prepare();
            if(transitionResult.isInvalid()) {
                return OrderActionResult.failed("Cannot prepare order");
            }
            statusNotifier.notifyPreparedOrder(order);
            return OrderActionResult.success();
        }
    }

    public Set<UUID> listPreparedOrders() {
        return orderRepository.getPreparedOrders();
    }

    public OrderActionResult<Object[]> deliverOrder(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return OrderActionResult.failed("Order not found");
        }
        synchronized (order) {
            StateTransitionResult transitionResult = order.deliver();
            if(transitionResult.isInvalid()) {
                return OrderActionResult.failed("Cannot prepare order");
            }
            List<String> pancakesToDeliver = viewOrder(order.getId());
            orderRepository.delete(orderId);
            statusNotifier.notifyDeliveredOrder(order);
            OrderLog.logDeliverOrder(order);
            return OrderActionResult.success(new Object[]{order, pancakesToDeliver});
        }
    }
}
