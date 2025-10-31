package com.dietiestates.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dietiestates.api.model.Offer;

public interface OfferRepository extends 
	CrudRepository<Offer, Long>,
	PagingAndSortingRepository<Offer, Long>{
	
	List<Offer> findByUser(String userEmail, Pageable pageable);
	List<Offer> findByRealEstate(Long realEstateId, Pageable pageable);
    List<Offer> findByRealEstateId(Long realEstateId, Pageable pageable);

	/*
    Page<Offer> findByEstateAgent_EmailAndRealEstate_IdOrderByCreatedDateDesc(
            String estateAgentEmail,
            Long realEstateId,
            Pageable pageable);
    */
}
