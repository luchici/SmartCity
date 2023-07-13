package com.github.luchici.authservice.repositories;

import com.github.luchici.authservice.model.Account;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {
    @Modifying
    @Query("DELETE FROM account WHERE account.username = :username;")
    boolean deleteByUsername(@Param("username") String username);

    Optional<Account> findByLoginDataUsername(String username);
}
