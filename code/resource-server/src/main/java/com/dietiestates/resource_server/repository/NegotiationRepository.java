package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends CrudRepository<Negotiation, Long> {
    boolean existsById(Long id);
    boolean existsByUserEmailAndRealEstateId(String userEmail, Long realEstateId);
    Optional<Negotiation> findByRealEstateIdAndUserId(Long realEstateId, Long userId);
    Optional<Negotiation> findByRealEstateIdAndEstateAgentId(Long realEstateId, Long estateAgentId);
    List<Negotiation> findByUserId(Long userId);
    List<Negotiation> findByEstateAgentId(Long estateAgentId);
}
