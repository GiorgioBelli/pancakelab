package org.pancakelab.notification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Notifier<T> {

    private final Map<String, Set<Subscriber<T>>> topicSubscribers = new ConcurrentHashMap<>();

    public void addSubscriber(Subscriber<T> subscriber) {
        for (String subject : subscriber.getSubject()) {
            Set<Subscriber<T>> subscribers = this.topicSubscribers.computeIfAbsent(subject, k -> new HashSet<>());
            subscribers.add(subscriber);
        }
    }

    public void notify(T update, String subject) {
        Set<Subscriber<T>> subscribers = this.topicSubscribers.getOrDefault(subject, Collections.emptySet());
        for (Subscriber<T> subscriber : subscribers) {
            subscriber.update(update);
        }
    }
}
