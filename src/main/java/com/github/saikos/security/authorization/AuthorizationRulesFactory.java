package com.github.saikos.security.authorization;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;

public interface AuthorizationRulesFactory<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {
    AuthorizationRules<S> newAuthorizationRules();
}
