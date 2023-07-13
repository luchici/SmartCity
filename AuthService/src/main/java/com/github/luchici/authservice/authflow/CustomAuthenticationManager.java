package com.github.luchici.authservice.authflow;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private final CustomAuthenticationProvider customAuthenticationProvider;

    // TODO: global exception handler for ProviderNotFoundException
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        if (customAuthenticationProvider.supports(authentication.getClass())) {

        }
        ;
        System.out.println("======================================================");
        System.out.println(this.getClass().getSimpleName());
        System.out.println("No suport " + authentication.getClass());
        System.out.println("======================================================");
        throw new ProviderNotFoundException("No suport for authtication ");
    }
}
