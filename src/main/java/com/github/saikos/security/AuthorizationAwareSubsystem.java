package com.github.saikos.security;

import com.github.saikos.security.authorization.AuthorizationPolicy;
import com.github.saikos.security.authorization.CapabilityAuthorizer;
import com.github.saikos.security.authorization.PermissionGrant;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityBuilder;

import java.util.Set;
import java.util.function.Supplier;

public interface AuthorizationAwareSubsystem<
    E extends EcosystemEnvironment, S extends Subject<?, ? extends Subsystem<E, S>>
> extends Subsystem<E, S> {

    PermissionValidator<?> getPermissionValidator();

    void setPermissionValidator(PermissionValidator<?> permissionValidator);

    /**
     * Each functional unit supports its own set of capabilities.
     */
    Set<String> getSupportedCapabilities();

    /**
     * Each functional unit has its own authorization policy
     */
    AuthorizationPolicy<S> getAuthorizationPolicy();

    void setAuthorizationPolicy(AuthorizationPolicy<S> authorizationPolicy);

    <C extends CapabilityBuilder<S, ? extends Capability<S>>> C capabilityProviderOf(String capabilityName);

    default boolean currentUserHasRole(PermissionGrant role) {
        return withCurrentUser((S user) -> user.getGrantedPermissions().isRoleGranted(role));
    }

    default void ensureCurrentUserHasRole(PermissionGrant role, Supplier<RuntimeException> exceptionSupplier) {
        if (!currentUserHasRole(role)) {
            throw exceptionSupplier.get();
        }
    }

    CapabilityAuthorizer<E, S> origin(Origin origin);


}
