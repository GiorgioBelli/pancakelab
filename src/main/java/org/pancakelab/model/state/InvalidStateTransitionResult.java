package org.pancakelab.model.state;

import org.pancakelab.model.StateTransitionResult;

public class InvalidStateTransitionResult implements StateTransitionResult {
    @Override
    public boolean isValid() {
        return false;
    }
}
