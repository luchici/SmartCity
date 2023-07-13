package com.github.luchici.authservice.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static com.github.luchici.authservice.model.AccountPermission.ADMIN_CREATE;
import static com.github.luchici.authservice.model.AccountPermission.ADMIN_DELETE;
import static com.github.luchici.authservice.model.AccountPermission.ADMIN_READ;
import static com.github.luchici.authservice.model.AccountPermission.ADMIN_UPDATE;
import static com.github.luchici.authservice.model.AccountPermission.USER_DELETE;
import static com.github.luchici.authservice.model.AccountPermission.USER_READ;
import static com.github.luchici.authservice.model.AccountPermission.USER_UPDATE;

public enum Role {
    ADMIN(0, Set.of(ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE)),
    USER(1, Set.of(USER_READ, USER_UPDATE, USER_DELETE));
    private final Set<AccountPermission> permissions;
    private final Integer roleId;

    Role(final Integer id, final Set<AccountPermission> permissions) {
        this.roleId = id;
        this.permissions = permissions;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Set<AccountPermission> getPermissions() {
        return permissions;
    }

    public SimpleGrantedAuthority getGrantedAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}
