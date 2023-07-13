package com.github.luchici.authservice.repositories;

import com.github.luchici.authservice.model.Role;
import com.github.luchici.authservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;

@Repository
@RequiredArgsConstructor
public class RoleRepository {
    @NonNull
    private static Set<Role> formRoleIdsToRoles(final List<Integer> roleIds) {
        switch (roleIds.size()) {
            case 2:
                return Set.of(ADMIN, USER);
            case 1:
                if (roleIds.get(0) == 0) {
                    return Set.of(ADMIN);
                } else {
                    return Set.of(USER);
                }
            default:
                throw new AppException("No roles for the user");
        }
    }
    private final JdbcTemplate jdbcTemplate;

    public Set<Role> getRolesByAccountId(final int accountId) {
        // TODO: Maybe final from string need to be replace
        final String sql = "SELECT role_id FROM account_role WHERE account=?";
        final List<Integer> roleIds = jdbcTemplate.queryForList(sql, Integer.class, accountId);

        return formRoleIdsToRoles(roleIds);
    }
}
