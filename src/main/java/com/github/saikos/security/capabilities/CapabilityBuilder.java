package com.github.saikos.security.capabilities;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;

import java.util.Map;

public interface CapabilityBuilder<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>,
    C extends Capability<S>
> {
    String getName();
    default C with(Map<String, ?> params) { throw new UnsupportedOperationException("Dynamic parameters are not supported for " + this); }
    // default BiConsumer<Object, InjectedCapability<U, C>> injector() { throw new UnsupportedOperationException("Not injectable"); }
}
