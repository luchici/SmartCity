package com.github.luchici.authservice.services;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.model.Role;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigBeans.class, AuthService.class})
class AuthServiceTest {
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    // @Autowired
    // private ModelMapper modelMapper;

    @Test
    void loginIsSuccessful() {
        LoginData loginData = new LoginData("DavidTheBeast", "12345");
        Set<GrantedAuthority> authorities = Arrays.stream(Role.values())
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toSet());
        var authToken = new UsernamePasswordAuthenticationToken(
                loginData.getUsername(),
                loginData.getPassword(),
                authorities);
        when(authenticationManager.authenticate(any())).thenReturn(authToken);
        assertDoesNotThrow(() -> authService.login(loginData));
    }

    @Test
    void loginFails_ByWrongUsername_And_UsernameNotFound() {
        LoginData loginData = new LoginData("David", "12345");
        Set<GrantedAuthority> authorities = Arrays.stream(Role.values())
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toSet());
        var authToken = new UsernamePasswordAuthenticationToken(
                loginData.getUsername(),
                loginData.getPassword(),
                authorities);
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        assertThrows(BadCredentialsException.class, () -> authService.login(loginData));
    }

    @Test
    void register() {
        Account account = AccountBuilder.builder()
                .withDefault()
                .build();
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        authService.register(newAccountDto);
        verify(accountRepository).save(account);
    }
}
