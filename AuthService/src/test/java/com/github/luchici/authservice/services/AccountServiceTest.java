package com.github.luchici.authservice.services;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.exceptions.AppException;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.repositories.AccountRepository;
import com.github.luchici.authservice.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigBeans.class, AccountService.class})
class AccountServiceTest {
    private static final Integer PAGE_SIZE = 5;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AccountService accountService;

    @Test
    void successfully_getUserByUsername() {
        // Given
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        expectedAccount.setId(1);
        Account accountWithoutRoles = AccountBuilder.builder()
                .withDefault()
                .withRoles(Collections.emptySet())
                .build();
        accountWithoutRoles.setId(1);
        String username = expectedAccount.getUsername();
        when(accountRepository.findByLoginDataUsername(username)).thenReturn(Optional.of(accountWithoutRoles));
        when(roleRepository.getRolesByAccountId(anyInt())).thenReturn(Set.of(USER, ADMIN));
        // When
        Account actualAccount = accountService.getAccountByUsername(username);
        // Then
        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    void getUserByUsername_ThrowUsernameNotFoundException_WhenNoUserWithThatUsername() {
        // Given
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        Account accountWithoutRoles = AccountBuilder.builder()
                .withDefault()
                .withRoles(Collections.emptySet())
                .build();
        String username = "someUniqueUsername";
        when(accountRepository.findByLoginDataUsername(username)).thenReturn(Optional.empty());
        when(roleRepository.getRolesByAccountId(anyInt())).thenReturn(Collections.emptySet());
        // Then
        assertThrows(UsernameNotFoundException.class, () -> accountService.getAccountByUsername(username));
    }

    @Test
    void successfullyGetAllAccounts_WhenUsersAreFound() {
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        Page<ResponseAccountDto> expectedPage = getResponseAccountDtosPage();
        Page<Account> accountPage = getAccountPage();
        when(accountRepository.findAll(pageable)).thenReturn(accountPage);
        when(roleRepository.getRolesByAccountId(anyInt())).thenReturn(Set.of(USER, ADMIN));
        Page<ResponseAccountDto> actualPage = accountService.getAllAccounts(1);
        assertAll(
                () -> assertEquals(expectedPage.getTotalPages(), actualPage.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent()));
    }

    @Test
    void getAllAccounts_ReturnEmptyPageList_WhenNoAccountsAreFound() {
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        Page<Account> expectedPage = Page.empty();
        when(accountRepository.findAll(pageable)).thenReturn(expectedPage);
        when(roleRepository.getRolesByAccountId(anyInt())).thenReturn(Set.of(USER, ADMIN));
        Page<ResponseAccountDto> actualPage = accountService.getAllAccounts(1);
        assertAll(
                () -> assertEquals(expectedPage.getTotalPages(), actualPage.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent()));
    }

    @Test
    void successfullyUpdateAnExistingAccount() {
        String oldUsername = "AlPacino";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        Account newAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        Account oldAccount = AccountBuilder.builder()
                .withDefault()
                .withUsername(oldUsername)
                .build();
        oldAccount.setId(1);
        when(accountRepository.findByLoginDataUsername(oldUsername)).thenReturn(Optional.of(oldAccount));
        assertFalse(accountService.updateOrCreateAccount(oldUsername, newAccountDto));
        verify(accountRepository)
                .save(argThat(acc -> oldAccount.getId().equals(acc.getId())));
}

    @Test
    void successfullyCreateAnAccount() {
        String oldUsername = "AlPacino";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        Account newAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        when(accountRepository.findByLoginDataUsername(oldUsername)).thenReturn(Optional.empty());
        assertTrue(accountService.updateOrCreateAccount(oldUsername, newAccountDto));
        verify(accountRepository)
                .save(argThat(acc -> newAccount.getId()==null));
    }

    @Test
    void save_ThrowsException_WhenAccountToBeSavedIsNull() {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        when(accountRepository.save(expectedAccount)).thenThrow(IllegalArgumentException.class);
        assertThrows(AppException.class, () -> accountService.save(newAccountDto));
    }

    @Test
    void successfullySaveNewAccount() {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        Account account = AccountBuilder.builder()
                .withDefault()
                .build();
        Account savedExpectedAccount = AccountBuilder.builder().withDefault().build();
        savedExpectedAccount.setId(1);
        when(accountRepository.save(account)).thenReturn(savedExpectedAccount);
        assertDoesNotThrow(() -> accountService.save(newAccountDto));
    }

    private Page<ResponseAccountDto> getResponseAccountDtosPage() {
        List<ResponseAccountDto> responseAccountDtos = List.of(
                ResponseAccountDtoBuilder.builder().withDefault().build(),
                ResponseAccountDtoBuilder.builder().withDefault().build(),
                ResponseAccountDtoBuilder.builder().withDefault().build(),
                ResponseAccountDtoBuilder.builder().withDefault().build(),
                ResponseAccountDtoBuilder.builder().withDefault().build());
        return new PageImpl<>(responseAccountDtos);
    }

    private Page<Account> getAccountPage() {
        Random random = new Random();
        List<Account> accounts = List.of(
                AccountBuilder.builder().withDefault().withRoles(Collections.emptySet()).build(),
                AccountBuilder.builder().withDefault().withRoles(Collections.emptySet()).build(),
                AccountBuilder.builder().withDefault().withRoles(Collections.emptySet()).build(),
                AccountBuilder.builder().withDefault().withRoles(Collections.emptySet()).build(),
                AccountBuilder.builder().withDefault().withRoles(Collections.emptySet()).build());
        accounts.stream().forEach(acc -> acc.setId(random.nextInt()));
        return new PageImpl<>(accounts);
    }
}
