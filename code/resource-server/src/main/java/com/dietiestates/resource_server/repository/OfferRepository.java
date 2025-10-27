package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Offer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OfferRepository extends 
	CrudRepository<Offer, Long>,
	PagingAndSortingRepository<Offer, Long>{
	
	List<Offer> findByUser(String userEmail, Pageable pageable);
	List<Offer> findByRealEstate(Long realEstateId, Pageable pageable);
	/*
    Page<Offer> findByEstateAgent_EmailAndRealEstate_IdOrderByCreatedDateDesc(
            String estateAgentEmail,
            Long realEstateId,
            Pageable pageable);
    */
}
