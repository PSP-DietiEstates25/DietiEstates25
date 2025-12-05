package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferFinder {
	Offer getOfferById(Long id) throws OfferNotFoundException;
    Page<Offer> getRealEstateUserOffers(Long realEstateId, Long userId, Pageable pageable);
    Page<Offer> getRealEstateEstateAgentOffers(Long realEstateId, Long estateAgentId, Pageable pageable);
    Page<Offer> getAllUserOffers(Long userId, Pageable pageable);
    Page<Offer> getAllEstateAgentOffers(Long estateAgentId, Pageable pageable);
    List<Offer> extractAllNegotiationsOffers(List<Negotiation> negotiations);
}
