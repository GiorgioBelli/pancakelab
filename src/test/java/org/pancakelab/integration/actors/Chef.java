package org.pancakelab.integration.actors;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.notification.Subscriber;
import org.pancakelab.notification.messages.StatusUpdate;
import org.pancakelab.service.PancakeService;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Chef implements Subscriber<StatusUpdate>, Entity, Callable<Void> {

    private final int id;
    private final PancakeService pancakeService;
    private final BlockingQueue<Order> readyToPrepareOrders;
    private final AtomicInteger processAmount;

    public Chef(int id, PancakeService pancakeService, int processAmount) {
        this.id = id;
        this.pancakeService = pancakeService;
        this.readyToPrepareOrders = new LinkedBlockingQueue<>();
        this.processAmount = new AtomicInteger(processAmount);
    }

    @Override
    public void update(StatusUpdate statusUpdate) {
        try {
            this.readyToPrepareOrders.put(statusUpdate.getOrder());
        } catch (InterruptedException e) {
            // TODO - refactor this part with a retry or something similar
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
    public Void call() throws Exception {
        while (processAmount.getAndDecrement() > 0 ) {
            try {
                Order order = this.readyToPrepareOrders.take();
                log("Preparing order: %s", order.getId());
                pancakeService.prepareOrder(order.getId());
            } catch (InterruptedException e) {
                // TODO - handle it
            }
        }
        return null;
    }
}
