package org.pancakelab.model.state;

import org.pancakelab.model.StateTransitionResult;

public class ValidStateTransitionResult implements StateTransitionResult {
    @Override
    public boolean isValid() {
        return true;
    }
}
