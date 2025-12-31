package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.RealEstateStatus;
import com.dietiestates.resource_server.model.Negotiation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends CrudRepository<Negotiation, Long> {

    //HELPER
    default Optional<Negotiation> findActiveByRealEstateIdAndUserId(Long realEstateId, Long userId){
        return findByRealEstate_IdAndUser_IdAndRealEstate_Status(realEstateId, userId, RealEstateStatus.ACTIVE);
    }

    default Optional<Negotiation> findActiveByRealEstateIdAndEstateAgentId(Long realEstateId, Long estateAgentId){
        return findByRealEstate_IdAndEstateAgent_IdAndRealEstate_Status(realEstateId, estateAgentId, RealEstateStatus.ACTIVE);
    }

    //for active real estates
    default List<Negotiation> findByUserIdAndActiveRealEstates(Long userId){
        return findByUser_IdAndRealEstate_Status(userId, RealEstateStatus.ACTIVE);
    }

    default List<Negotiation> findByEstateAgentIdAndActiveRealEstates(Long estateAgentId){
        return findByEstateAgent_IdAndRealEstate_Status(estateAgentId, RealEstateStatus.ACTIVE);
    }


    //for all real estates
    default List<Negotiation> findByUserIdAndAllRealEstates(Long userId){
        return findByUser_Id(userId);
    }

    default List<Negotiation> findByEstateAgentIdAndAllRealEstates(Long estateAgentId){
        return findByEstateAgent_Id(estateAgentId);
    }

    //QUERY
    boolean existsById(Long id);
    boolean existsByUserEmailAndRealEstateId(String userEmail, Long realEstateId);

    Optional<Negotiation> findByRealEstate_IdAndUser_IdAndRealEstate_Status(Long realEstateId, Long userId, RealEstateStatus status);
    Optional<Negotiation> findByRealEstate_IdAndEstateAgent_IdAndRealEstate_Status(Long realEstateId, Long estateAgentId, RealEstateStatus status);

    //for active real estates
    List<Negotiation> findByUser_IdAndRealEstate_Status(Long userId, RealEstateStatus status);
    List<Negotiation> findByEstateAgent_IdAndRealEstate_Status(Long estateAgentId, RealEstateStatus status);

    //for all real estates
    List<Negotiation> findByUser_Id(Long userId);
    List<Negotiation> findByEstateAgent_Id(Long estateAgentId);
}
