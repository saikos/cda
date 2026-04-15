package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityValidationException extends CapabilityException {

    public CapabilityValidationException(String message, Capability<? extends Subject<?, ?>> capability) {
        super(message, null, capability);
    }

    public CapabilityValidationException(String message, Throwable cause, Capability<? extends Subject<?, ?>> capability) {
        super(message, cause, capability);
    }
}
