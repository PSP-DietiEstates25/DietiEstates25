package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisitRepository extends CrudRepository<Visit, Long>, PagingAndSortingRepository<Visit, Long> {
    boolean existsById(Long id);
    boolean existsByIdAndNegotiationId(Long id, Long negotiationId);
    Page<Visit> findByNegotiation_RealEstate_Id(Long realEstateId, Pageable pageable);
}
