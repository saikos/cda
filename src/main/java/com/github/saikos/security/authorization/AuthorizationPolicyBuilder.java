package com.github.saikos.security.authorization;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.AuthorizationAwareSubsystem;
import com.github.saikos.security.CapabilityActivation;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class AuthorizationPolicyBuilder<
    E extends EcosystemEnvironment,
    S extends Subject<? extends SubjectData, ? extends AuthorizationAwareSubsystem<E, S>>
> {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationPolicyBuilder.class);

    private Rule<S, ? extends Capability<S>> defaultRule = AuthorizationRules.denyAllRule("default DenyAll Rule");
    private boolean denyAllWhenUserIsDisabled = true;
    private boolean denyAllWhenUserIsNotAuthenticated = true;
    private boolean grantAllWhenUserIsAdmin = true;
    private Set<String> capabilitiesToExcludeFromAdmin = null;
    private final List<Consumer<AuthorizationRulesBuilder<S>>> customRules = new ArrayList<>();
    private final Map<String, CapabilityActivation<S>> capabilityActivations = new LinkedHashMap<>();

    public AuthorizationPolicyBuilder<E, S> withDefaultRule(Rule<S, ? extends Capability<S>> rule) {
        this.defaultRule = rule;
        return this;
    }

    public AuthorizationPolicyBuilder<E, S> denyAllWhenUserIsDisabled(boolean deny) {
        this.denyAllWhenUserIsDisabled = deny;
        return this;
    }

    public AuthorizationPolicyBuilder<E, S> denyAllWhenUserIsNotAuthenticated(boolean deny) {
        this.denyAllWhenUserIsNotAuthenticated = deny;
        return this;
    }

    public AuthorizationPolicyBuilder<E, S> grantAllWhenUserIsAdmin(boolean grant) {
        this.grantAllWhenUserIsAdmin = grant;
        return this;
    }

    public AuthorizationPolicyBuilder<E, S> grantAllWhenUserIsAdminExcluding(Set<String> capabilitiesToExclude) {
        this.grantAllWhenUserIsAdmin = true;
        this.capabilitiesToExcludeFromAdmin = capabilitiesToExclude;
        return this;
    }

    /**
     * The rules defined through this method take precedence over the ones defined through the withCapability method
     * (they are applied before any capability rules)
     * @param rulesCustomizer a customizer of the authorization rules
     * @return the authorization policy builder
     */
    public AuthorizationPolicyBuilder<E, S> withRule(Consumer<AuthorizationRulesBuilder<S>> rulesCustomizer) {
        customRules.add(rulesCustomizer);
        return this;
    }

    public AuthorizationPolicyBuilder<E, S> withCapability(
            CapabilityBuilder<S, ? extends Capability<S>> capabilityProvider,
            Consumer<AuthorizationRulesBuilder<S>> rulesCustomizer
    ) {
        return withCapability(capabilityProvider.getName(), capabilityProvider, rulesCustomizer);
    }

    public AuthorizationPolicyBuilder<E, S> withCapability(
        String capabilityName,
        CapabilityBuilder<S, ? extends Capability<S>> capabilityProvider,
        Consumer<AuthorizationRulesBuilder<S>> rulesCustomizer
    ) {
        if(capabilityName == null || capabilityProvider == null || !capabilityName.equals(capabilityProvider.getName())) {
            throw new IllegalArgumentException("Invalid capability name: " + capabilityName);
        }
//        logger.debug("CapabilityActivation for capability name: {} provider: {} is {} ", capabilityName, capabilityProvider.getName(), capabilityProvider);
        capabilityActivations.put(capabilityName, CapabilityActivation.of(capabilityProvider, rulesCustomizer));
        return this;
    }

    public AuthorizationPolicy<S> build(E environment) {
        return new AuthorizationPolicy<S>() {
            @Override
            public List<? extends CapabilityBuilder<S, ? extends Capability<S>>> capabilityBuilders() {
                return capabilityActivations.values().stream().map(CapabilityActivation::provider).toList();
            }

            @Override
            public AuthorizationRules<S> forSubject(S user) {
                if (!user.isEnabled() && denyAllWhenUserIsDisabled) {
                    return new AuthorizationRules<S>(AuthorizationRules.denyAllRule("User is not enabled"));
                }

                if (!user.isAuthenticated() && denyAllWhenUserIsNotAuthenticated) {
                    return new AuthorizationRules<S>(AuthorizationRules.denyAllRule("User is not authenticated"));
                }

                if (isAdminUser(user) && grantAllWhenUserIsAdmin) {
                    if (capabilitiesToExcludeFromAdmin == null || capabilitiesToExcludeFromAdmin.isEmpty()) {
                        return new AuthorizationRules<S>(AuthorizationRules.grantAllRule("User is admin && grantAllWhenUserIsAdmin is true"));
                    }
                    else {
                        customRules.add(0, builder -> {
                            for(String capabilityName: user.getSubsystem().getSupportedCapabilities()) {
                                if (!capabilitiesToExcludeFromAdmin.contains(capabilityName)) {
                                    builder.grant(
                                        capabilityName,
                                        "Granting " + capabilityName + "to admin",
                                        capability -> isAdminUser(capability.getSubject())
                                    );
                                }
                            }
                        });

                    }
                }

                return authorizationRulesForUser(user);
            }
        };
    }

    private AuthorizationRules<S> authorizationRulesForUser(S user) {
        AuthorizationRulesBuilder<S> builder = new AuthorizationRulesBuilder<>();
        customRules.forEach( ruleCustomizer -> ruleCustomizer.accept(builder));
        capabilityActivations.forEach(  (String capabilityName, CapabilityActivation<S> capabilityActivation) ->
                capabilityActivation.rulesForAuthorizingCapability().accept(builder)
        );

        logger.debug("UserName: {}, FU: {}, capabilityActivationsNames: {}",user.getName(), user.getSubsystem().getName(), capabilityActivations.keySet().stream().sorted().toList());
        return builder.build(defaultRule);
    }

    private boolean isAdminUser(S user) {
        return user.getGrantedPermissions().isRoleGranted(Permissions.ADMIN);
    }
}
