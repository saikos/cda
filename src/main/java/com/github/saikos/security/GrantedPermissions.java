package com.github.saikos.security;

import com.github.saikos.security.authorization.PermissionGrant;

public interface GrantedPermissions {

    boolean isRoleGranted(PermissionGrant permissionGrant);

    boolean isOneOfRolesGranted(PermissionGrant... permissionGrants);
}
