package com.dietiestates.authorization.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.authorization.model.DefaultAccount;

public interface DefaultAccountRepository extends CrudRepository<DefaultAccount, Long> {

	Optional<DefaultAccount> findByEmail(String email);
}
