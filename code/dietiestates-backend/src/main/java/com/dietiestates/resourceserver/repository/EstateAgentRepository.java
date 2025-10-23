package com.dietiestates.resourceserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.model.EstateAgent;

public interface EstateAgentRepository extends CrudRepository<EstateAgent, Long>{

	Optional<EstateAgent> findByEmail(String email);
}
