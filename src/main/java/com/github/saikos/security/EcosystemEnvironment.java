package com.github.saikos.security;

import com.github.saikos.I18N;
import com.github.saikos.object.StorageCoordinates;
import com.github.saikos.security.authorization.PermissionGrant;

import java.util.Set;

/**
 * An environment holding information about users, settings and other application-wide data.
 */
public interface EcosystemEnvironment {

    I18N getI18n();

    StorageCoordinates anonymousUserCoords();

    StorageCoordinates adminUserCoords();

    /**
     * The permission grants supported in the environment (for all the subsystems).
     * Each subsystem may support a different subset of these permissions.
     */
    Set<? extends PermissionGrant> getSupportedPermissionTypes();

    GrantedPermissions permissionsGrantedToAnonymous();

}
