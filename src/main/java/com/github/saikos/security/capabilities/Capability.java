package com.github.saikos.security.capabilities;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Origin;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;

import java.util.function.Consumer;

public interface Capability<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {

    Origin getOrigin();

    void setOrigin(Origin origin);

    boolean isReadOnly();

    S getSubject();

    void setUser(S user);

    default void validateBeforeAuthorizing() throws CapabilityValidationException {}

    default void validateBeforeGranting() throws CapabilityValidationException {}

    default void withProvision(Consumer<CapabilityProvision<S, ? extends Capability<S>>> resolver) {}

    String getName();

}
