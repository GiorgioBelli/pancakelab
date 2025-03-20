package org.pancakelab.notification;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;

public record OrderUpdate(Order order, OrderStatus orderStatus) {
}
