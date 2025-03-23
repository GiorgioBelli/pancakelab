package org.pancakelab.integration.actors;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderActionResult;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.service.PancakeService;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Disciple implements Callable<Void>, Entity {

    private final int id;
    private final PancakeService pancakeService;
    private final AtomicInteger processAmount;

    public Disciple(int id, PancakeService pancakeService, int processAmount) {
        this.id = id;
        this.pancakeService = pancakeService;
        this.processAmount = new AtomicInteger(processAmount);
    }

    @Override
    public Void call() throws Exception {
        while (processAmount.getAndDecrement() > 0 ) {
            OrderActionResult<Order> createResult = pancakeService.createOrder(10, 10);
            if (createResult.isFailed()) {
                log("Cannot create order: %s", createResult.getMessage());
            }
            Order order = createResult.getReturnObject();
            log("Created order: %s", order.getId());
            pancakeService.addPancake(order.getId(), PancakeFactory.darkChocolateWhippedCreamHazelnutsPancake(), 1);
            pancakeService.addPancake(order.getId(), PancakeFactory.milkChocolatePancake(), 1);
            pancakeService.addPancake(order.getId(), PancakeFactory.milkChocolateHazelnutsPancake(), 1);
            log("Added pancakes to order %s: %s", order.getId(), order.view());
            pancakeService.completeOrder(order.getId());
            log("Completed order: %s", order.getId());
        }
        return null;
    }

    @Override
    public int id() {
        return this.id;
    }
}
