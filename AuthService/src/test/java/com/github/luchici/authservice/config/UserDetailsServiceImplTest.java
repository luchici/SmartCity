package com.github.luchici.authservice.config;

import com.github.luchici.authservice.authflow.UserDetailsServiceImpl;
import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.exceptions.UsernameNotFoundExceptionWrapper;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDetailsServiceImpl.class)
class UserDetailsServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void successfullyLoadUserByUsername() {
        String username = "Isabela";
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        when(accountRepository.findByLoginDataUsername(username)).thenReturn(Optional.of(expectedAccount));
        Account actualAccount = userDetailsService.loadUserByUsername(username);
        assertEquals(expectedAccount,actualAccount);
    }

    @Test
    void loadUserByUsername_ThrowUsernameNotFoundException() {
        String username = "LeonardoDiCaprio";
        var expectedException = new UsernameNotFoundExceptionWrapper(username);
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        when(accountRepository.findByLoginDataUsername(username)).thenThrow(new UsernameNotFoundExceptionWrapper(username));
        UsernameNotFoundException actualException =
                assertThrows(UsernameNotFoundException.class, ()->userDetailsService.loadUserByUsername(username));
        assertEquals(expectedException.getMessage(),actualException.getMessage());
    }

}
