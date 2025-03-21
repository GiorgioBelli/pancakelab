package org.pancakelab.notification;

import java.util.List;

public interface Subscriber<T> {

    void update(T orderUpdate);
    List<String> getSubject();

}
