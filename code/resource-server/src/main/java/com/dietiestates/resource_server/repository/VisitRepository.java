package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.RealEstateStatus;
import com.dietiestates.resource_server.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisitRepository extends CrudRepository<Visit, Long>, PagingAndSortingRepository<Visit, Long> {

    default Page<Visit> findActiveByRealEstateId(Long realEstateId, Pageable pageable){
        return findByNegotiation_RealEstate_IdAndNegotiation_RealEstate_Status(realEstateId, RealEstateStatus.ACTIVE, pageable);
    }

    //QUERY
    boolean existsById(Long id);
    boolean existsByIdAndNegotiationId(Long id, Long negotiationId);
    Page<Visit> findByNegotiation_RealEstate_IdAndNegotiation_RealEstate_Status(Long realEstateId, RealEstateStatus status, Pageable pageable);
}
