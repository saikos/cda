package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityException extends RuntimeException {

    protected final Capability<? extends Subject<?, ?>> capability;

    public CapabilityException(String message, Throwable cause, Capability<? extends Subject<?, ?>> capability) {
        super(message, cause);
        this.capability = capability;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ": " + capability;
    }
}
