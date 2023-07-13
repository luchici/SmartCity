package com.github.luchici.authservice.model;

public enum AccountPermission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");
    private final String permission;

    AccountPermission(final String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
