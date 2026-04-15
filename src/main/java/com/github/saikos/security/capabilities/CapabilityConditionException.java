package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityConditionException extends CapabilityException {

    public CapabilityConditionException(Throwable cause, Capability<? extends Subject<?, ?>> capability) {
        super(null, cause, capability);
    }

    @Override
    public String getMessage() {
        return "Evaluating the condition of " + capability + " threw an error: " + getCause();
    }
}
