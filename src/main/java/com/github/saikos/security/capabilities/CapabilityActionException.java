package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityActionException extends CapabilityException {

    public CapabilityActionException(String message, Capability<? extends Subject<?, ?>> capability) {
        super(message, null, capability);
    }

    public CapabilityActionException(Throwable cause, Capability<? extends Subject<?, ?>> capability) {
        super(null, cause, capability);
    }

    @Override
    public String getMessage() {
        if (getCause() == null) {
            return "An unexpected internal error occurred in the action of granted capability " + capability + ": " + super.getMessage();
        }
        else {
            return "Executing an action on granted " + capability + " threw an error: " + getCause();
        }

    }
}
