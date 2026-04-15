package com.github.saikos.security.authorization;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;
import com.github.saikos.security.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class AuthorizationRulesBuilder<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>
> {

    private final List<Rule<S, ? extends Capability<S>>> rules = new ArrayList<>();

    public AuthorizationRules<S> build(Rule<S, ? extends Capability<S>> defaultRule) {
        AuthorizationRules<S> authorizationRules = new AuthorizationRules<>(defaultRule);
        authorizationRules.rules.addAll(rules);
        return authorizationRules;
    }

    <C extends Capability<S>> void addRule(RuleType type, String applicableForCapability, String description, Predicate<C> condition) {
        rules.add(new Rule<>(type, applicableForCapability, description, condition));
    }

    public <C extends Capability<S>> AuthorizationRulesBuilder<S> transform(String applicableForCapability,
                                                                      Predicate<C> condition,
                                                                      Function<Capability<S>, C> transformer) {
        rules.add(
            new Rule<>(
                RuleType.GRANT_TRANSFORMED,
                applicableForCapability,
                applicableForCapability,
                condition,
                transformer
            )
        );
        return this;
    }

    public <C extends Capability<S>> AuthorizationRulesBuilder<S> transform(String applicableForCapability,
                                                                            String description,
                                                                            Predicate<C> condition,
                                                                            Function<Capability<S>, C> transformer) {
        rules.add(
            new Rule<>(
                RuleType.GRANT_TRANSFORMED,
                applicableForCapability,
                description,
                condition,
                transformer
            )
        );
        return this;
    }

    public <P extends Capability<S>> AuthorizationRulesBuilder<S> grant(String applicableForCapability, Predicate<P> condition) {
        return grant(applicableForCapability, applicableForCapability, condition);
    }

    public <P extends Capability<S>> AuthorizationRulesBuilder<S> grant(String applicableForCapability, String description, Predicate<P> condition) {
        addRule(RuleType.GRANT, applicableForCapability, description, condition);
        return this;
    }

    public <P extends Capability<S>> AuthorizationRulesBuilder<S> grantMany(Set<String> applicableForCapabilities, Predicate<P> condition) {
        applicableForCapabilities.forEach( capability -> {
            addRule(RuleType.GRANT, capability, capability, condition);
        });
        return this;
    }

    public <P extends Capability<S>> AuthorizationRulesBuilder<S> grantMany(Set<String> applicableForCapabilities, Set<String> descriptions, Predicate<P> condition) {
        //TODO(odysseas) enable this descriptions
        grantMany(applicableForCapabilities, condition);
        return this;
    }

    public <P extends Capability<S>> AuthorizationRulesBuilder<S> deny(String applicableForCapability, Predicate<P> condition) {
        addRule(RuleType.DENY, applicableForCapability, applicableForCapability, condition);
        return this;
    }

    public <P extends Capability<S>> AuthorizationRulesBuilder<S> deny(String applicableForCapability, String description, Predicate<P> condition) {
        addRule(RuleType.DENY, applicableForCapability, description, condition);
        return this;
    }
}
