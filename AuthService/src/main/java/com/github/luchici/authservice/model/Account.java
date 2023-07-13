package com.github.luchici.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.luchici.authservice.model.embedded.AccountState;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.model.embedded.UserData;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;
import static org.springframework.data.relational.core.mapping.Embedded.OnEmpty.USE_EMPTY;

@Data
@Table("account")
@Validated
public class Account implements UserDetails {
    @Id
    @Column("account_id")
    private Integer id;
    @NotNull
    @Valid
    @Embedded(onEmpty = USE_EMPTY)
    private LoginData loginData;
    @Valid
    @NotNull
    @Size(min = 1,max = 2)
    // @Column("name")
    @MappedCollection(idColumn = "account", keyColumn = "role_id")
    private Set<RoleRef> roles = new HashSet<>();
    @Valid
    @NotNull
    @Embedded(onEmpty = USE_EMPTY)
    private AccountState state;
    @Valid
    @NotNull
    @Embedded(onEmpty = USE_EMPTY)
    private UserData userData;

    public Account() {
        this.state = new AccountState(
                true,
                true,
                true,
                true);
    }

    public Account(final LoginData loginData, final Set<RoleRef> roles, final AccountState state, final UserData userData) {
        this.loginData = loginData;
        this.roles = roles;
        this.state = state;
        this.userData = userData;
    }

    public Set<Role> getRoles() {
        return this.roles.stream().map(role -> role.getRole() == 0 ? ADMIN : USER).collect(Collectors.toSet());
    }

    public void setRoles(@NotNull Collection<Role> roles) {
        this.roles = roles.stream()
                .map(role -> new RoleRef(role.getRoleId()))
                .collect(Collectors.toSet());
    }

    public void addRole(final Role role) {
        this.roles.add(new RoleRef(role.getRoleId()));
    }

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(roleRef -> new SimpleGrantedAuthority(
                        Arrays.stream(Role.values())
                                .filter(role -> role.getRoleId().equals(roleRef.getRole())).findFirst().get().name()))
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return loginData.getPassword();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return loginData.getUsername();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return state.getIsAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return state.getIsAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return state.getIsCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return state.getIsEnabled();
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginData.getUsername(), userData.getEmail());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final Account account = (Account) o;
        return Objects.equals(loginData.getUsername(), account.loginData.getUsername()) &&
                Objects.equals(userData.getEmail(), account.userData.getEmail());
    }

    public boolean fullyEquals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final Account account = (Account) o;
        return Objects.equals(loginData.getUsername(), account.loginData.getUsername()) &&
                Objects.equals(roles, account.roles) &&
                Objects.equals(state, account.state) &&
                Objects.equals(userData, account.userData);
    }
}
