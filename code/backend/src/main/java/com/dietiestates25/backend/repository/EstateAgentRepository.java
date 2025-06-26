package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.EstateAgent;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateAgentRepository extends JpaRepository<EstateAgent, String> {
    EstateAgent findByEmail(String email);
    List<EstateAgent> findByAgencyId(Long agencyId);
}
