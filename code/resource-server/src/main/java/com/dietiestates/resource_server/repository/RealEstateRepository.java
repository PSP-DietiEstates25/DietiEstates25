package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.RealEstate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RealEstateRepository extends CrudRepository<RealEstate, Long>, PagingAndSortingRepository<RealEstate, Long>{
    boolean existsById(Long id);
    boolean existsByIdAndEstateAgentEmail(Long id, String estateAgentEmail);
    Page<RealEstate> findAll(Pageable pageable);
}
