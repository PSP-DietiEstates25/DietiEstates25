package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.EstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EstateAgentRepository extends CrudRepository<EstateAgent, Long>{
    boolean existsById(Long id);
	Optional<EstateAgent> findByEmail(String email);
    Page<EstateAgent> findByAdminId(Long id, Pageable pageable);
}
