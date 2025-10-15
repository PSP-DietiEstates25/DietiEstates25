package com.authenticationserver.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.authenticationserver.api.model.DefaultAccount;

public interface DefaultAccountRepository extends CrudRepository<DefaultAccount, Long> {

	Optional<DefaultAccount> findByEmail(String email);
}
