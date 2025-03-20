package org.pancakelab.notification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Notifier {

    private final Map<String, Set<Subscriber>> topicSubscribers = new ConcurrentHashMap<>();

    private void addSubscriber(Subscriber subscriber, String subject) {
        Set<Subscriber> subscribers = this.topicSubscribers.computeIfAbsent(subject, k -> new HashSet<>());
        subscribers.add(subscriber);
    }

    public void addSubscriber(Subscriber subscriber) {
        for (String subject : subscriber.getSubject()) {
            this.addSubscriber(subscriber, subject);
        }
    }

    private void notify(OrderUpdate orderUpdate, String subject) {
        Set<Subscriber> subscribers = this.topicSubscribers.getOrDefault(subject, Collections.emptySet());
        for (Subscriber subscriber : subscribers) {
            subscriber.update(orderUpdate);
        }
    }

    public void notify(OrderUpdate orderUpdate) {
        this.notify(orderUpdate, orderUpdate.orderStatus().toString());
    }
}
