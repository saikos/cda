package com.github.saikos.security.authorization;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;
import com.github.saikos.security.capabilities.Capability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorizationRules<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {

    protected static final Logger logger = LoggerFactory.getLogger(AuthorizationRules.class);

    public final Rule<S, ? extends Capability<S>> defaultRule;
    protected final List<Rule<S, ? extends Capability<S>>> rules = new ArrayList<>();

    public AuthorizationRules(Rule<S, ? extends Capability<S>> defaultRule) {
        this.defaultRule = defaultRule;
    }

    public Stream<Rule<S, ? extends Capability<S>>> applicableFor(String capabilityName) {
        return rules.stream().filter( r -> r.applicableForCapability == null || r.applicableForCapability.equals(capabilityName));
    }

    public static <
        S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>,
        C extends Capability<S>
    > Rule<S, C> denyAllRule(String description) {
        return new Rule<>(RuleType.DENY, null, description, c -> true);
    }

    public static <
        S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>,
        C extends Capability<S>
    > Rule<S, C> grantAllRule(String description) {
        return new Rule<>(RuleType.GRANT, null, description, c -> true);
    }

    @Override
    public String toString() {
        return rules.stream().map( Object::toString).collect(Collectors.joining("\n"));
    }
}
