package org.pancakelab.log;

import org.pancakelab.notification.Subscriber;
import org.pancakelab.notification.messages.OrderUpdate;
import org.pancakelab.service.OrderLog;

public abstract class LogSubscriber<T extends OrderUpdate> implements Subscriber<T> {

    protected final OrderLog log;

    protected LogSubscriber(OrderLog log) {
        this.log = log;
    }
}
