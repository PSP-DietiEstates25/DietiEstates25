package com.dietiestates.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.User;

public interface DefaultAccountRepository extends CrudRepository<DefaultAccount, Long>{

	Optional<DefaultAccount> findByEmail(String email);
}
