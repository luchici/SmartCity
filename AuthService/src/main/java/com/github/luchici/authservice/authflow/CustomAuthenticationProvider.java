package com.github.luchici.authservice.authflow;

import com.github.luchici.authservice.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();

        final Account databaseAccount = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(
                authentication.getCredentials().toString(),
                databaseAccount.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    databaseAccount.getUsername(),
                    databaseAccount.getPassword(),
                    databaseAccount.getAuthorities());
        } else {
            throw new BadCredentialsException("The user " + username + " try to log with a wrong password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
