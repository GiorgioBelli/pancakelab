package org.pancakelab.notification;

import java.util.List;

public interface Subscriber<T> {

    <V extends T> void update(V update);
    List<String> getSubject();

}
