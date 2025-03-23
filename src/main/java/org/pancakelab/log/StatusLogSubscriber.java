package org.pancakelab.log;

import org.pancakelab.model.OrderStatus;
import org.pancakelab.notification.messages.StatusUpdate;
import org.pancakelab.service.OrderLog;

import java.util.List;

public class StatusLogSubscriber extends LogSubscriber<StatusUpdate> {

    public StatusLogSubscriber(OrderLog log) {
        super(log);
    }

    @Override
    public void update(StatusUpdate update) {
        this.log.statusUpdate(update);
    }

    @Override
    public List<String> getSubject() {
        return List.of(
                OrderStatus.DELIVERED.toString(),
                OrderStatus.CANCELED.toString());
    }
}
