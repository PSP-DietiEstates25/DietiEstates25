package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.Offer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferRepository extends CrudRepository<Offer, Long> {
	/*
    Page<Offer> findByEstateAgent_EmailAndRealEstate_IdOrderByCreatedDateDesc(
            String estateAgentEmail,
            Long realEstateId,
            Pageable pageable);
    */
}
