package com.github.saikos.security.authorization;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityBuilder;

import java.util.List;

public interface AuthorizationPolicy<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {

    List<? extends CapabilityBuilder<S, ? extends Capability<S>>> capabilityBuilders();

    AuthorizationRules<S> forSubject(S user);

}
