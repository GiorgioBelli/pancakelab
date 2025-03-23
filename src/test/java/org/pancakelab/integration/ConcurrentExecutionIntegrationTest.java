package org.pancakelab.integration;

import org.junit.jupiter.api.Test;
import org.pancakelab.integration.actors.Chef;
import org.pancakelab.integration.actors.DeliveryGuy;
import org.pancakelab.integration.actors.Disciple;
import org.pancakelab.log.PancakeLogSubscriber;
import org.pancakelab.log.StatusLogSubscriber;
import org.pancakelab.notification.PancakesNotifier;
import org.pancakelab.notification.StatusNotifier;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.service.OrderLog;
import org.pancakelab.service.PancakeService;
import org.pancakelab.validation.CreateOrderValidator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrentExecutionIntegrationTest {

    @Test
    public void testConcurrentOrderCreation() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        StatusNotifier statusNotifier = new StatusNotifier();
        PancakesNotifier pancakesNotifier = new PancakesNotifier();
        OrderLog orderLog = new OrderLog();
        PancakeLogSubscriber pancakeLogSubscriber = new PancakeLogSubscriber(orderLog);
        StatusLogSubscriber statusLogSubscriber = new StatusLogSubscriber(orderLog);
        StatusValidator statusValidator = new StatusValidator();
        PancakeService service = new PancakeService(
                new OrderRepository(), new CreateOrderValidator(), statusNotifier, pancakesNotifier
        ); // this could be a builder for more flexibility

        Disciple disciple1 = new Disciple(1, service, 20);
        Disciple disciple2 = new Disciple(2, service, 20);
        Disciple disciple3 = new Disciple(3, service, 20);
        Chef chef = new Chef(1, service, 60);
        DeliveryGuy deliveryguy = new DeliveryGuy(1, service, 60);

        statusNotifier.addSubscriber(chef);
        statusNotifier.addSubscriber(deliveryguy);
        statusNotifier.addSubscriber(statusLogSubscriber);
        statusNotifier.addSubscriber(statusValidator);
        pancakesNotifier.addSubscriber(pancakeLogSubscriber);

        List<Future<Void>> futures = executor.invokeAll(List.of(chef, deliveryguy, disciple1, disciple2, disciple3), 5, TimeUnit.SECONDS);
        for (Future<Void> future : futures) {
            assertTrue(future.isDone());
            assertFalse(future.isCancelled());
        }

        assertTrue(statusValidator.isValid());
    }
}
