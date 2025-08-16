package com.dietiestates.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.Token;

public interface TokenRepository extends CrudRepository<Token, Integer>{

	Optional<Token> findByToken(String token);
}
