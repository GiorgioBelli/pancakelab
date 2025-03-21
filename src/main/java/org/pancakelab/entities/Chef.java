package org.pancakelab.entities;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.notification.Subscriber;
import org.pancakelab.notification.messages.StatusUpdate;
import org.pancakelab.service.PancakeService;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Chef implements Subscriber<StatusUpdate>, Entity, Runnable {

    private final int id;
    private final PancakeService pancakeService;
    private final BlockingQueue<Order> readyToPrepareOrders;

    public Chef(int id, PancakeService pancakeService) {
        this.id = id;
        this.pancakeService = pancakeService;
        this.readyToPrepareOrders = new LinkedBlockingQueue<>();
    }

    @Override
    public void update(StatusUpdate statusUpdate) {
        try {
            this.readyToPrepareOrders.put(statusUpdate.getOrder());
        } catch (InterruptedException e) {
            // TODO - refactor this part with a retry or something similar
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSubject() {
        return List.of(OrderStatus.COMPLETED.toString());
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = this.readyToPrepareOrders.take();
                Thread.sleep(2000);
                log("Preparing order: %s", order.getId());
                pancakeService.prepareOrder(order.getId());
            } catch (InterruptedException e) {
                // TODO - handle it
            }
        }
    }
}
