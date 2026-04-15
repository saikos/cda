package com.github.saikos.security.capabilities;

import com.github.saikos.security.Subject;

public class CapabilityTransformException extends CapabilityException {

    public CapabilityTransformException(Throwable cause, Capability<? extends Subject<?, ?>> capability) {
        super(null, cause, capability);
    }

    @Override
    public String getMessage() {
        return "Transformation of " + capability + " threw an error: " + getCause();
    }
}
