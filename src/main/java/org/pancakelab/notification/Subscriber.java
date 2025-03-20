package org.pancakelab.notification;

import java.util.List;

public interface Subscriber {

    void update(OrderUpdate orderUpdate);
    List<String> getSubject();

}
