package org.pancakelab.entities;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderActionResult;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.service.PancakeService;

public class Disciple implements Runnable, Entity {

    private final int id;
    private final PancakeService pancakeService;

    public Disciple(int id, PancakeService pancakeService) {
        this.id = id;
        this.pancakeService = pancakeService;
    }

    @Override
    public void run() {
        while (true) {
            OrderActionResult<Order> createResult = pancakeService.createOrder(10, 10);
            if (createResult.isFailed()) {
                log("Cannot create order: %s", createResult.getMessage());
            }
            Order order = createResult.getReturnObject();
            log("Created order: %s", order.getId());
            try {
                Thread.sleep(3000);
                pancakeService.addPancake(order.getId(), PancakeFactory.darkChocolateWhippedCreamHazelnutsPancake(), 1);
                pancakeService.addPancake(order.getId(), PancakeFactory.milkChocolatePancake(), 1);
                pancakeService.addPancake(order.getId(), PancakeFactory.milkChocolateHazelnutsPancake(), 1);
                log("Added pancakes to order %s: %s", order.getId(), order.view());
                Thread.sleep(3000);
                pancakeService.completeOrder(order.getId());
                log("Completed order: %s", order.getId());
            } catch (InterruptedException e) {
                // TODO - handle it
            }
        }
    }

    @Override
    public int id() {
        return this.id;
    }
}
