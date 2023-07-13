package com.github.luchici.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Table("account_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRef {
    @Column("role_id")
    @PositiveOrZero
    @Max(1)
    private Integer role;
}
