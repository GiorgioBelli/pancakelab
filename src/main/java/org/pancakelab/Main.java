package org.pancakelab;

import org.pancakelab.entities.Chef;
import org.pancakelab.entities.DeliveryGuy;
import org.pancakelab.entities.Disciple;
import org.pancakelab.log.PancakeLogSubscriber;
import org.pancakelab.log.StatusLogSubscriber;
import org.pancakelab.notification.PancakesNotifier;
import org.pancakelab.notification.StatusNotifier;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.service.OrderLog;
import org.pancakelab.service.PancakeService;
import org.pancakelab.validation.CreateOrderValidator;

public class Main {
    public static void main(String[] args) {
        StatusNotifier statusNotifier = new StatusNotifier();
        PancakesNotifier pancakesNotifier = new PancakesNotifier();
        OrderLog orderLog = new OrderLog();
        PancakeLogSubscriber pancakeLogSubscriber = new PancakeLogSubscriber(orderLog);
        StatusLogSubscriber statusLogSubscriber = new StatusLogSubscriber(orderLog);
        PancakeService service = new PancakeService(
                new OrderRepository(), new CreateOrderValidator(), statusNotifier, pancakesNotifier
        ); // this could be a builder for more flexibility

        Disciple disciple1 = new Disciple(1, service);
        Disciple disciple2 = new Disciple(2, service);
        Disciple disciple3 = new Disciple(3, service);
        Chef chef = new Chef(1, service);
        DeliveryGuy deliveryguy = new DeliveryGuy(1, service);

        statusNotifier.addSubscriber(chef);
        statusNotifier.addSubscriber(deliveryguy);
        statusNotifier.addSubscriber(statusLogSubscriber);
        pancakesNotifier.addSubscriber(pancakeLogSubscriber);

        new Thread(disciple1).start();
        new Thread(disciple2).start();
        new Thread(disciple3).start();
        new Thread(chef).start();
        new Thread(deliveryguy).start();

    }
}