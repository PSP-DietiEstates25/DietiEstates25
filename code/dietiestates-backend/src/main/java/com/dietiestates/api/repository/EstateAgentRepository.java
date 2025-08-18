package com.dietiestates.api.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.EstateAgent;

public interface EstateAgentRepository extends CrudRepository<EstateAgent, Long> {
    Optional<EstateAgent> findByUser_Email(String email);

    Optional<EstateAgent> findByUserId(Long userId);
}
