package com.github.luchici.authservice.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AccountState {
    @NotNull
    private Boolean isAccountNonExpired;
    @NotNull
    private Boolean isAccountNonLocked;
    @NotNull
    private Boolean isCredentialsNonExpired;
    @NotNull
    private Boolean isEnabled;

    public AccountState() {
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }
}
