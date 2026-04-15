package com.github.saikos.security.authorization;

import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;
import com.github.saikos.security.capabilities.Capability;
import org.slf4j.Logger;

import java.util.function.Function;

public interface CapabilityAuthorizer<
    E extends EcosystemEnvironment,
    S extends Subject<?, ? extends Subsystem<E, S>>
> {

    Logger getLogger();

    default <C extends Capability<S>> boolean isGiven(C capability) {
        try {
            return given(capability, c -> true);
        } catch (Exception e) {
            getLogger().warn("Capability " + capability + " is not given: " + e.getMessage(), e);
            return false;
        }
    }

    <C extends Capability<S>, O> O given(C capability, Function<C, O> action);
}
