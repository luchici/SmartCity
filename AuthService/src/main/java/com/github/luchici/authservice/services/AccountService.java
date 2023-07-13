package com.github.luchici.authservice.services;

import com.github.luchici.authservice.model.Role;
import com.github.luchici.authservice.exceptions.AppException;
import com.github.luchici.authservice.exceptions.UsernameNotFoundExceptionWrapper;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.repositories.AccountRepository;
import com.github.luchici.authservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final Integer PAGE_SIZE = 5;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public Account getAccountByUsername(final String username) {
        final Account account = accountRepository.findByLoginDataUsername(username)
                .orElseThrow(() -> new UsernameNotFoundExceptionWrapper("No user found with this username=" + username));
        final var roles = roleRepository.getRolesByAccountId(account.getId());
        account.setRoles(roles);
        return account;
    }

    public Page<ResponseAccountDto> getAllAccounts(final int pageNumber) {
        // Humans count from 1, pages start from 0
        final Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        final Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(this::convertAccountToResponseDto);
    }

    public void save(final NewAccountDto newAccountDto) {
        final Account account = modelMapper.map(newAccountDto, Account.class);
        try {
            accountRepository.save(account);
        } catch (final IllegalArgumentException ex) {
            throw new AppException(ex);
        }
    }

    public boolean updateOrCreateAccount(final String oldUsername, final NewAccountDto newAccountDto) {
        final Account newAccount = modelMapper.map(newAccountDto, Account.class);
        final Optional<Account> oldAccount = accountRepository.findByLoginDataUsername(oldUsername);
        if (oldAccount.isPresent()) {
            newAccount.setId(oldAccount.get().getId());
            accountRepository.save(newAccount);
            return false;
        }
        accountRepository.save(newAccount);
        return true;
    }

    public void deleteAccount(final String username) {
        accountRepository.deleteByUsername(username);
    }

    private ResponseAccountDto convertAccountToResponseDto(final Account account) {
        final Set<Role> roles = roleRepository.getRolesByAccountId(account.getId());
        final ResponseAccountDto accountDto = modelMapper.map(account, ResponseAccountDto.class);
        accountDto.setRoles(roles);
        return accountDto;
    }
}
