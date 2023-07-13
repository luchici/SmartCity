package com.github.luchici.authservice.repositories;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.model.Role;
import com.github.luchici.authservice.exceptions.AppException;
import com.github.luchici.authservice.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.Set;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({RoleRepository.class})
@ActiveProfiles("repo-test")
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyGetRolesByAccountId() {
        Set<Role> expectedRoles = Set.of(USER, ADMIN);
        Set<Role> actualRoles = roleRepository.getRolesByAccountId(26);
        assertEquals(expectedRoles,actualRoles);
    }

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyUserRoleByAccountId() {
        Set<Role> expectedRoles = Set.of(USER);
        Set<Role> actualRoles = roleRepository.getRolesByAccountId(10);
        assertEquals(expectedRoles,actualRoles);
    }

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyAdminRoleByAccountId() {
        Set<Role> expectedRoles = Set.of(ADMIN);
        Set<Role> actualRoles = roleRepository.getRolesByAccountId(8);
        assertEquals(expectedRoles,actualRoles);
    }

    @Test
    void exceptionThrownWhenNoRolesForDatabaseUser() {
        Account account = AccountBuilder.builder()
                .withDefault()
                .withRoles(Collections.emptySet())
                .build();
        Account savedAccount = accountRepository.save(account);
        assertThrows(AppException.class,
                ()->roleRepository.getRolesByAccountId(savedAccount.getId()));
    }
}
