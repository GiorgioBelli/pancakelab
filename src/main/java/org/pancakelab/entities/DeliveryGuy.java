package org.pancakelab.entities;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.notification.Subscriber;
import org.pancakelab.notification.messages.StatusUpdate;
import org.pancakelab.service.PancakeService;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DeliveryGuy implements Subscriber<StatusUpdate>, Entity, Runnable {

    private final int id;
    private final PancakeService pancakeService;
    private final BlockingQueue<Order> deliverableOrders;

    public DeliveryGuy(int id, PancakeService pancakeService) {
        this.id = id;
        this.pancakeService = pancakeService;
        this.deliverableOrders = new LinkedBlockingQueue<>();
    }

    @Override
    public void update(StatusUpdate statusUpdate) {
        try {
            this.deliverableOrders.put(statusUpdate.getOrder());
        } catch (InterruptedException e) {
            // TODO - refactor this part with a retry or something similar
        }
    }

    @Override
    public List<String> getSubject() {
        return List.of(OrderStatus.PREPARED.toString());
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = this.deliverableOrders.take();
                log("Delivering order: %s", order.getId());
                Thread.sleep(5000);
                pancakeService.deliverOrder(order.getId());
            } catch (InterruptedException e) {
                // TODO - handle it
            }
        }
    }
}
