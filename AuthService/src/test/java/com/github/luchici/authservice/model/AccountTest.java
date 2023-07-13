package com.github.luchici.authservice.model;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.model.embedded.AccountState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {
    private Account account;
    private Account secondAccount;
    private Account thirdAccount;
    private ResponseAccountDto responseAccountDto;

    @BeforeEach
    void setUp() {
        account = AccountBuilder.builder()
                .withDefault()
                .build();
        ;
        secondAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        thirdAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        responseAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .build();
    }

    @Test
    void successfullyGetAuthorities() {
        Collection<GrantedAuthority> expectedAuthorities = Set.of(
                new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("USER"));
        Collection<GrantedAuthority> actualAuthorities = account.getAuthorities();
        assertEquals(expectedAuthorities, actualAuthorities);
    }

    @Test
    void equals_ReturnTrue_WhenUsernameAndEmailOfTwoAccountsAreEqual() {
        secondAccount.setState(new AccountState(false, true, false, true));
        thirdAccount.setState(new AccountState(false, false, false, false));
        assertAll("equals() Contract",
                () -> assertEquals(account, account),
                () -> assertEquals(secondAccount, secondAccount),
                () -> assertEquals(thirdAccount, thirdAccount),
                () -> assertEquals(account, secondAccount),
                () -> assertEquals(secondAccount, account),
                () -> assertEquals(secondAccount, thirdAccount),
                () -> assertEquals(thirdAccount, account));
    }

    @Test
    void equals_ReturnFalse_WhenUsernameOfTwoAccountsAreNotEqual() {
        secondAccount.getLoginData().setUsername("secondUniqueUsername");
        thirdAccount.getLoginData().setUsername("thirdUniqueUsername");
        assertAll("equals() Contract",
                () -> assertNotEquals(account, secondAccount),
                () -> assertNotEquals(secondAccount, account),
                () -> assertNotEquals(secondAccount, thirdAccount),
                () -> assertNotEquals(thirdAccount, account));
    }

    @Test
    void equals_ReturnFalse_WhenObjectsAreCompatible() {
        assertNotEquals(account,responseAccountDto);
    }

    @Test
    void fullyEquals_ReturnTrue_WhenUsernameAndEmailOfTwoAccountsAreEqual() {
        assertAll("equals() Contract",
                () -> assertTrue(account.fullyEquals(account)),
                () -> assertTrue(secondAccount.fullyEquals(secondAccount)),
                () -> assertTrue(thirdAccount.fullyEquals(thirdAccount)),
                () -> assertTrue(account.fullyEquals(secondAccount)),
                () -> assertTrue(secondAccount.fullyEquals(account)),
                () -> assertTrue(secondAccount.fullyEquals(thirdAccount)),
                () -> assertTrue(thirdAccount.fullyEquals(account)));
    }

    @Test
    void fullyEquals_ReturnFalse_WhenUsernameOfTwoAccountsAreEqualNutNotTheState() {
        secondAccount.setState(new AccountState(false, true, false, true));
        thirdAccount.setState(new AccountState(false, false, false, false));
        assertAll("equals() Contract",
                () -> assertFalse(account.fullyEquals(secondAccount)),
                () -> assertFalse(secondAccount.fullyEquals(account)),
                () -> assertFalse(secondAccount.fullyEquals(thirdAccount)),
                () -> assertFalse(thirdAccount.fullyEquals(account)));
    }

    @Test
    void fullyEquals_ReturnFalse_WhenUsernameOfTwoAccountsAreNotEqual() {
        secondAccount.getLoginData().setUsername("secondUniqueUsername");
        thirdAccount.getLoginData().setUsername("thirdUniqueUsername");
        assertAll("equals() Contract",
                () -> assertFalse(account.fullyEquals(secondAccount)),
                () -> assertFalse(secondAccount.fullyEquals(account)),
                () -> assertFalse(secondAccount.fullyEquals(thirdAccount)),
                () -> assertFalse(thirdAccount.fullyEquals(account)));
    }

    @Test
    void fullyEquals_ReturnFalse_WhenObjectsAreCompatible() {
        assertFalse(account.fullyEquals(responseAccountDto));
    }
}
