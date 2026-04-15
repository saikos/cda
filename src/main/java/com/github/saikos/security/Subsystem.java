package com.github.saikos.security;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityProvision;

import java.util.function.Function;

/**
 * A subsystem of an application ecosystem (a functional unit of the ecosystem, such as an app, an API etc.)
 * that operates using its own authentication & authorization rules.
 */
public interface Subsystem<
    E extends EcosystemEnvironment,
    S extends Subject<? extends SubjectData, ? extends Subsystem<E, S>>
> extends Sudo<S> {

    String getName();

    E getEnvironment();

    /**
     * Create a Subject from the given SubjectData object.
     */
    S createUser(SubjectData userData);

    /**
     * Get the anonymous (not authenticated) user.
     */
    S anonymousUser();

    /**
     * Get the "system" user, a privileged user similar to the root in unix systems
     */
    S systemUser();

    S retrieveCurrentUser();

    /**
     * Sudo
     */
    <O> O withUser(S user, Function<S, O> action);
//    default <O> O withUser(U user, Function<U, O> action) {
//        return action.apply(user);
//    }

    default <O> O withCurrentUser(Function<S, O> action) {
        return withUser(retrieveCurrentUser(), action);
    }

    default <O> O withSystemUser(Function<S, O> action) {
        return withUser(systemUser(), action);
    }

    void resolve(CapabilityProvision<S, ? extends Capability<S>> cp);
}
