package com.github.luchici.authservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    void testAdminGetGrantedAuthority() {
        SimpleGrantedAuthority expectedRoleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
        SimpleGrantedAuthority actualRoleAdmin  = ADMIN.getGrantedAuthority();
        assertEquals(expectedRoleAdmin,actualRoleAdmin);
    }
}
