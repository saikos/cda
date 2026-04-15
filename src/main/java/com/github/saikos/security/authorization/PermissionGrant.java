package com.github.saikos.security.authorization;

public interface PermissionGrant {

    String name();
    boolean isRole();
    boolean isReadOnly();

    static PermissionGrant ofRole(String name, boolean readOnly) {
        return new PermissionGrant() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean isRole() {
                return true;
            }

            @Override
            public boolean isReadOnly() {
                return readOnly;
            }
        };
    }
}
