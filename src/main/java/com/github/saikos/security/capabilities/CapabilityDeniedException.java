package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityDeniedException extends CapabilityException {

    public CapabilityDeniedException(Capability<? extends Subject<?, ?>> capability) {
        super(null, null, capability);
    }

    @Override
    public String getMessage() {
        return capability.getSubject().getName() + " is not authorized for " + capability;
    }
}
