package org.pancakelab.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.pancakes.PancakeFactory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository repository;

    private final List<Order> orders = List.of(
            new Order(100, 10).addPancake(PancakeFactory.milkChocolatePancake()),
            new Order(200, 20).addPancake(PancakeFactory.darkChocolateWhippedCreamHazelnutsPancake()),
            new Order(300, 30).addPancake(PancakeFactory.darkChocolatePancake())
    );

    @BeforeEach
    void setupEach() {
        repository = new OrderRepository();
        for (Order order : orders) {
            repository.upsertOrder(order);
        }

        repository.updateStatus(orders.get(0).getId(), OrderStatus.CREATED);
        repository.updateStatus(orders.get(1).getId(), OrderStatus.PREPARED);
        repository.updateStatus(orders.get(2).getId(), OrderStatus.COMPLETED);
    }

    @Test
    void getOrders() {
        List<Order> savedOrders = repository.getOrders();
        orders.forEach(expOrd -> assertTrue(savedOrders.contains(expOrd)));
        savedOrders.forEach(savedOrder -> assertTrue(orders.contains(savedOrder)));
    }

    @Test
    void getCompletedOrders() {
        Set<UUID> completedOrders = repository.getCompletedOrders();
        assertEquals(1, completedOrders.size());
        assertTrue(completedOrders.contains((orders.get(2).getId())));
    }

    @Test
    void getPreparedOrders() {
        Set<UUID> preparedOrders = repository.getPreparedOrders();
        assertEquals(1, preparedOrders.size());
        assertTrue(preparedOrders.contains((orders.get(1).getId())));
    }

    @Test
    void getOrderById_WhenOrderExists_ThenOrder() {
        Order order = repository.getOrderById(orders.get(0).getId());
        assertNotNull(order);
        assertEquals(orders.get(0), order);
    }

    @Test
    void getOrderById_WhenOrderExists_ThenNull() {
        Order order = repository.getOrderById(UUID.randomUUID());
        assertNull(order);
    }

    @Test
    void GivenOrder_WhenUpsert_ThenIsAvailable() {
        int initialCount = repository.getOrders().size();
        Order newOrder = new Order(400,40);
        repository.upsertOrder(newOrder);
        List<Order> orders = repository.getOrders();
        assertEquals(initialCount+1, orders.size());
        assertTrue(orders.contains(newOrder));

    }

    @Test
    void GivenOrder_WhenDeleted_ThenIsNotAvailable() {
        Order orderToRemove = orders.get(1);
        repository.delete(orderToRemove.getId());
        List<Order> orders = repository.getOrders();
        Set<UUID> preparedOrders = repository.getPreparedOrders();

        assertFalse(orders.contains(orderToRemove));
        assertFalse(preparedOrders.contains(orderToRemove.getId()));
    }

    @Test
    void GivenOrder_WhenChangeStatus_ThenStatusHasChanged() {
        Order orderToUpdate = orders.get(0);
        int completedOrdersCount = repository.getCompletedOrders().size();
        repository.updateStatus(orderToUpdate.getId(), OrderStatus.COMPLETED);
        Set<UUID> completedOrders = repository.getCompletedOrders();
        assertTrue(completedOrders.contains(orderToUpdate.getId()));
        assertEquals(completedOrdersCount+1, completedOrders.size());
    }

    @Test
    void GivenOrders_WhenHasStatus_ThenTrue() {
        assertTrue(repository.hasStatus(orders.get(0).getId(), OrderStatus.CREATED));
        assertTrue(repository.hasStatus(orders.get(1).getId(), OrderStatus.PREPARED));
        assertTrue(repository.hasStatus(orders.get(2).getId(), OrderStatus.COMPLETED));
    }
}