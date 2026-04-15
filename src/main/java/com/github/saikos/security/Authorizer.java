package com.github.saikos.security;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityDeniedException;

public interface Authorizer<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {
     <C extends Capability<S>> C authorize(C capability) throws CapabilityDeniedException;
}