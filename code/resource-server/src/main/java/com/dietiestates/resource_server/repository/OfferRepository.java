package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends 
	CrudRepository<Offer, Long>,
	PagingAndSortingRepository<Offer, Long>{

    boolean existsById(Long id);

    boolean existsByIdAndRealEstateId(Long id, Long realEstateId);
	
	List<Offer> findByUser(String userEmail, Pageable pageable);

	Page<Offer> findByRealEstateId(Long realEstateId, Pageable pageable);

    Optional<Offer> findByIdAndRealEstateId(Long id, Long realEstateId);

	/*
    Page<Offer> findByEstateAgent_EmailAndRealEstate_IdOrderByCreatedDateDesc(
            String estateAgentEmail,
            Long realEstateId,
            Pageable pageable);
    */
}
