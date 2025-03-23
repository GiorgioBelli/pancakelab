package org.pancakelab.notification;

import java.util.List;

public interface Subscriber<T> {

    void update(T update);
    List<String> getSubject();

}
