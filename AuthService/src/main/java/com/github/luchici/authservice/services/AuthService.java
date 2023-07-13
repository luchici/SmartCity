package com.github.luchici.authservice.services;

import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void login(final LoginData loginData) {
        final Authentication authToken = new UsernamePasswordAuthenticationToken(
                loginData.getUsername(),
                loginData.getPassword());
        final Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void register(final NewAccountDto newAccountDto) {
        final String encodedPassword = passwordEncoder.encode(newAccountDto.getPassword());
        newAccountDto.setPassword(encodedPassword);
        final Account account = modelMapper.map(newAccountDto, Account.class);
        accountRepository.save(account);
    }
}
