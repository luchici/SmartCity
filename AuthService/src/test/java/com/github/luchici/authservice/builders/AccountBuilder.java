package com.github.luchici.authservice.builders;

import com.github.luchici.authservice.model.Role;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.RoleRef;
import com.github.luchici.authservice.model.embedded.AccountState;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.model.embedded.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;

public class AccountBuilder {
    private static AccountBuilder accountBuilder;
    private LoginData loginData;
    private Set<RoleRef> roleRefs;
    private AccountState state;
    private UserData userData;


    public static AccountBuilder builder() {
        accountBuilder = new AccountBuilder();
        return accountBuilder;
    }

    public AccountBuilder withDefault() {
        withDeafutlLoginData();
        withRoles(Set.of(USER, ADMIN));
        withDefaultAccountState();
        withDefaultUserData();
        return accountBuilder;
    }

    public AccountBuilder withLoginData(LoginData loginData) {
        this.loginData = loginData;
        return accountBuilder;
    }

    public AccountBuilder withDeafutlLoginData() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.loginData = new LoginData("DavidTheBeast", passwordEncoder.encode("12345"));
        return accountBuilder;
    }

    public AccountBuilder withRoles(Set<Role> roles) {
        this.roleRefs = roles.stream().map(r->new RoleRef(r.getRoleId())).collect(Collectors.toSet());
        return accountBuilder;
    }

    public AccountBuilder withRoleRefs(Set<RoleRef> roleRefs) {
        this.roleRefs = roleRefs;
        return accountBuilder;
    }

    public AccountBuilder withAccountState(AccountState state) {
        this.state = state;
        return accountBuilder;
    }

    public AccountBuilder withDefaultAccountState() {
        this.state = new AccountState(true, true, true, true);
        return accountBuilder;
    }

    public AccountBuilder withUserData(UserData userData) {
        this.userData = userData;
        return accountBuilder;
    }

    public AccountBuilder withDefaultUserData() {
        this.userData = UserDataBuilder.builder().withDefault().build();
        return accountBuilder;
    }

    public AccountBuilder withUsername(String username) {
        this.loginData.setUsername(username);
        return accountBuilder;
    }

    public AccountBuilder withPassword(String password) {
        this.loginData.setPassword(password);
        return accountBuilder;
    }


    public Account build() {
        return new Account(loginData, roleRefs, state, userData);
    }
}
