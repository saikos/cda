package com.github.saikos.security;

import java.util.Optional;

public final class PermissionGrantInMemory {
    public String permission;
    public Optional<String> containerCoords = Optional.empty();
    public Optional<String> unitCoords = Optional.empty();

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Optional<String> getContainerCoords() {
        return containerCoords;
    }

    public void setContainerCoords(Optional<String> containerCoords) {
        this.containerCoords = containerCoords;
    }

    @Override
    public String toString() {
        return "PermissionGrant{" +
                "permission='" + permission + '\'' +
                ", containerCoords=" + containerCoords +
                '}';
    }
}