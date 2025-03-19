package org.pancakelab.model;

import java.util.Objects;

public enum OrderStatus {
    CREATED,
    COMPLETED,
    PREPARED,
    DELIVERED;

    public boolean canChangeTo(OrderStatus next) {
        if (Objects.isNull(next)) {
            return false;
        }
        return next.ordinal() - this.ordinal() == 1;
    }

    public boolean canBeCanceled() {
        return this.equals(CREATED);
    }
}
