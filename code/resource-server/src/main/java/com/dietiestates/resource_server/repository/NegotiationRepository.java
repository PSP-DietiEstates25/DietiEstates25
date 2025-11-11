package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NegotiationRepository extends CrudRepository<Negotiation, Long> {
    boolean existsById(Long id);
    boolean existsByUserEmailAndRealEstateId(String userEmail, Long realEstateId);
    boolean existsByRealEstateId(Long realEstateId);
    boolean existsByEstateAgentEmail(String email);
    boolean existsByUserEmail(String email);
    Optional<Negotiation> findByRealEstate(RealEstate realEstate);
    Optional<Negotiation> findByUserAndRealEstate(User user, RealEstate realEstate);
    Optional<Negotiation> findByEstateAgentAndRealEstate(EstateAgent estateAgent, RealEstate realEstate);
}
