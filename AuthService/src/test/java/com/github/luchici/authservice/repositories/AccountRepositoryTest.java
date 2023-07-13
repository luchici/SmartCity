package com.github.luchici.authservice.repositories;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.exceptions.UsernameNotFoundExceptionWrapper;
import com.github.luchici.authservice.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("repo-test")
class AccountRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void returnAccount_ByFindByLoginDataUsername() {
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .withRoles(Collections.emptySet())
                .build();
        Optional<Account> actualAccount = accountRepository.findByLoginDataUsername(expectedAccount.getUsername());
        assertAll(
                () -> assertTrue(actualAccount.isPresent()),
                () -> assertEquals(expectedAccount, actualAccount.get()));
    }

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void emptyOptionalWhenUsernameNotFound_ByFindByLoginDataUsername() {
        assertTrue(accountRepository.findByLoginDataUsername("itDoesntMatter").isEmpty());
    }

    @Test
    void successfullySaveValidUser() {
        // Given
        Integer accountRowBeforeInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account", Integer.class);
        Integer accountRoleRowBeforeInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account_role", Integer.class);
        assumeTrue(Integer.valueOf(0).equals(accountRowBeforeInsertion), () -> "account table is not empty");
        assumeTrue(Integer.valueOf(0).equals(accountRoleRowBeforeInsertion), () -> "account_role table is not empty");
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .build();

        // When
        Account actualAccount = accountRepository.save(expectedAccount);

        // Then
        Integer accountRowAfterInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account", Integer.class);
        Integer accountRoleRowAfterInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account_role", Integer.class);
        assertAll(
                () -> assertEquals(expectedAccount, actualAccount),
                () -> assertNotNull(actualAccount.getId()),
                () -> assertEquals(Integer.valueOf(1), accountRowAfterInsertion, () -> "Number of row in account table is incorrect"),
                () -> assertEquals(Integer.valueOf(2), accountRoleRowAfterInsertion, () -> "Number of row in account_role table is incorrect"));
    }

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void failToSaveUserWithDuplicateUsernameAndUsername() {
        // Given
        Account duplicateAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        Account databaseAccount = accountRepository.findByLoginDataUsername(duplicateAccount.getUsername())
                .orElseThrow(()->new UsernameNotFoundExceptionWrapper(duplicateAccount.getUsername()));
        assumeTrue(duplicateAccount.equals(databaseAccount), () -> "Default account couldn't be found in database");

        // When
        Exception ex = assertThrows(DbActionExecutionException.class, () -> accountRepository.save(duplicateAccount));

        // Then
        assertAll(
                () -> assertNotNull(ex),
                () -> assertEquals(DuplicateKeyException.class, ex.getCause().getClass()),
                () -> assertTrue(ex.getMessage().contains("InsertRoot")));
    }

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyDeleteByUsername() {
        // Given
        int accountRowBeforeInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account", Integer.class);
        int accountRoleRowBeforeInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account_role", Integer.class);
        assumeTrue(Integer.valueOf(0).compareTo(accountRowBeforeInsertion) < 0, () -> "account table is empty");
        assumeTrue(Integer.valueOf(0).compareTo(accountRoleRowBeforeInsertion) < 0, () -> "account_role table is empty");
        Account toBeDeleteAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        // When
        boolean wasDeleted = accountRepository.deleteByUsername(toBeDeleteAccount.getUsername());

        // Then
        Integer accountRowAfterInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account", Integer.class);
        Integer accountRoleRowAfterInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account_role", Integer.class);
        assertAll(
                ()->assertTrue(wasDeleted),
                () -> assertEquals(accountRowBeforeInsertion - 1, accountRowAfterInsertion, () -> "Incorrect number of rows was deleted from the account table"),
                () -> assertEquals(accountRoleRowBeforeInsertion - 2, accountRoleRowAfterInsertion, () -> "Incorrect number of rows was deleted from account_role"));
    }

    @Test
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void deleteByLoginDataUsername() {
        // Given
        int accountRowBeforeInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account", Integer.class);
        int accountRoleRowBeforeInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account_role", Integer.class);
        assumeTrue(Integer.valueOf(0).compareTo(accountRowBeforeInsertion) < 0, () -> "account table is empty");
        assumeTrue(Integer.valueOf(0).compareTo(accountRoleRowBeforeInsertion) < 0, () -> "account_role table is empty");
        // When
        boolean wasDeleted = accountRepository.deleteByUsername("someUniqueUsername");
        // Then
        Integer accountRowAfterInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account", Integer.class);
        Integer accountRoleRowAfterInsertion = jdbcTemplate.queryForObject("SELECT count(*) FROM account_role", Integer.class);
        assertAll(
                ()->assertFalse(wasDeleted),
                () -> assertEquals(accountRowBeforeInsertion, accountRowAfterInsertion, () -> "Some rows were wrongfully deleted from the account table"),
                () -> assertEquals(accountRoleRowBeforeInsertion, accountRoleRowAfterInsertion, () -> "Some rows were wrongfully deleted from the account_role table"));
    }
}
