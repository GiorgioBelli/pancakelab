package org.pancakelab;

import org.pancakelab.entities.Chef;
import org.pancakelab.entities.DeliveryGuy;
import org.pancakelab.entities.Disciple;
import org.pancakelab.notification.OrderNotifier;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.service.PancakeService;
import org.pancakelab.validation.CreateOrderValidator;

public class Main {
    public static void main(String[] args) {
        OrderNotifier notifier = new OrderNotifier();
        PancakeService service = new PancakeService(
                new OrderRepository(), new CreateOrderValidator(), notifier
        ); // this could be a builder for more flexibility

        Disciple disciple1 = new Disciple(1, service);
        Disciple disciple2 = new Disciple(2, service);
        Disciple disciple3 = new Disciple(3, service);
        Chef chef = new Chef(1, service);
        DeliveryGuy deliveryguy = new DeliveryGuy(1, service);

        notifier.addSubscriber(chef);
        notifier.addSubscriber(deliveryguy);

        new Thread(disciple1).start();
        new Thread(disciple2).start();
        new Thread(disciple3).start();
        new Thread(chef).start();
        new Thread(deliveryguy).start();

    }
}