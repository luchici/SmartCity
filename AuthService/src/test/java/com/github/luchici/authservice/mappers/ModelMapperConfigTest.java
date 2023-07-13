package com.github.luchici.authservice.mappers;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConfigBeans.class)
class ModelMapperConfigTest {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    void successfullyConversionNewAccountDtoToAccount() {
        Account expectedAccount = AccountBuilder.builder()
                .withDefault()
                .build();
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        Account actualAccount = modelMapper.map(newAccountDto, Account.class);
        assertTrue(expectedAccount.fullyEquals(actualAccount));
    }

    @Test
    void successfullyConversionAccountToResponseAccountDto() {
        ResponseAccountDto expectedResponseAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .withRoles(ADMIN, USER)
                .build();
        Account account = AccountBuilder.builder()
                .withDefault()
                .build();
        ResponseAccountDto actualResponseAccountDto = modelMapper.map(account, ResponseAccountDto.class);
        assertEquals(expectedResponseAccountDto, actualResponseAccountDto);
    }
}
