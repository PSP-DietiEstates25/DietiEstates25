package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferFinder {

	Offer getOfferById(Long id) throws OfferNotFoundException;

    Page<Offer> getPagedRealEstateOffers(Long realEstateId, Pageable pageable);

    Offer getOfferByRealEstate(Long offerId, Long realEstateId);
}
