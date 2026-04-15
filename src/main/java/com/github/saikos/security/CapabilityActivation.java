package com.github.saikos.security;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.authorization.AuthorizationRulesBuilder;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityBuilder;

import java.util.function.Consumer;

public interface CapabilityActivation<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {

    default String name() {
        return provider().getName();
    }

    CapabilityBuilder<S, ? extends Capability<S>> provider();

    Consumer<AuthorizationRulesBuilder<S>> rulesForAuthorizingCapability();

    static <
        S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
    > CapabilityActivation<S> of(
        CapabilityBuilder<S, ? extends Capability<S>> provider,
        Consumer<AuthorizationRulesBuilder<S>> authRules
    ) {
        return new CapabilityActivation<S>() {
            @Override
            public CapabilityBuilder<S, ? extends Capability<S>> provider() {
                return provider;
            }

            @Override
            public Consumer<AuthorizationRulesBuilder<S>> rulesForAuthorizingCapability() {
                return authRules;
            }
        };
    }
}
