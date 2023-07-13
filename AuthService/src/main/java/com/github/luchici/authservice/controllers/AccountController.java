package com.github.luchici.authservice.controllers;

import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth/users")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private static final String STRING_INPUT_VALIDATION_ERROR = "please use just letters and digits, no less than 4 characters and no more that 25 characters";
    private static final String DIGITS_AND_LETTERS = "^([A-Za-z0-9]{4,25})$";
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<Account> getUserByUsername(
            @PathVariable @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR) String username) {
        final Account account = accountService.getAccountByUsername(username);
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ResponseAccountDto>> getAllUsers(
            @RequestParam(defaultValue = "1") @Min(value = 1L, message = "the min value is 1") final int pageNumber) {
        final Page<ResponseAccountDto> accountDtoPage = accountService.getAllAccounts(pageNumber);
        return ResponseEntity
                .status(OK)
                .header("Total Pages", String.valueOf(accountDtoPage.getTotalPages()))
                .header("Total Elements", String.valueOf(accountDtoPage.getTotalElements()))
                .body(accountDtoPage.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseAccountDto> createNewUser(@RequestBody @Valid final NewAccountDto newAccountDto) {
        accountService.save(newAccountDto);
        final ResponseAccountDto responseAccountDto = modelMapper.map(newAccountDto, ResponseAccountDto.class);
        return ResponseEntity
                .created(URI.create("/api/auth/users/" + newAccountDto.getUsername()))
                .body(responseAccountDto);
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<ResponseAccountDto> updateOrCreateUser(
            @PathVariable @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR) final String username,
            @RequestBody @Valid final NewAccountDto newAccountDto) {
        final boolean isNewUser = accountService.updateOrCreateAccount(username, newAccountDto);
        final ResponseAccountDto responseAccountDto = modelMapper.map(newAccountDto, ResponseAccountDto.class);
        if (isNewUser) {
            return ResponseEntity
                    .created(URI.create("/user/" + newAccountDto.getUsername()))
                    .body(responseAccountDto);
        }
        return ResponseEntity.ok(responseAccountDto);
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR) final String username) {
        accountService.deleteAccount(username);
        return ResponseEntity.noContent().build();
    }
}
