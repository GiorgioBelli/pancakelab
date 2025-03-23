package org.pancakelab.log;

import org.pancakelab.notification.messages.PancakeUpdateAction;
import org.pancakelab.notification.messages.PancakesUpdate;
import org.pancakelab.service.OrderLog;

import java.util.List;

public class PancakeLogSubscriber extends LogSubscriber<PancakesUpdate> {

    public PancakeLogSubscriber(OrderLog log) {
        super(log);
    }

    @Override
    public void update(PancakesUpdate update) {
        this.log.pancakesUpdate(update);
    }

    @Override
    public List<String> getSubject() {
        return List.of(
                PancakeUpdateAction.ADD.toString(),
                PancakeUpdateAction.REMOVE.toString());
    }
}
