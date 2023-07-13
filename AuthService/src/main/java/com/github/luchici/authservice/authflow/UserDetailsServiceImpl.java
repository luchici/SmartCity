package com.github.luchici.authservice.authflow;

import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    // TODO: Global controller UsernameNotFoundException
    @Override
    public Account loadUserByUsername(final String username) throws UsernameNotFoundException {
        return accountRepository.findByLoginDataUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
