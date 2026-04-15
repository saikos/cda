package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityProvisionException extends CapabilityException {

    public CapabilityProvisionException(String message, Capability<? extends Subject<?, ?>> capability) {
        super(message, null, capability);
    }

    @Override
    public String getMessage() {
        return "At least one of the provisions of " + capability + " is not granted: " + super.getMessage();
    }
}
