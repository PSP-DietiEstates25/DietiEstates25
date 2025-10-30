package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.spec.OfferSpec;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;


public interface OfferService {

	OfferResponse createOffer(OfferRequest request, Long realEstateId, Authentication authentication);
	
	OfferResponse getOfferById(Long realEstateId, Long offerId);

    Page<OfferResponse> getPagedRealEstateOffers(Long realEstateId, Integer page, Integer size);

    OfferResponse updateOfferStatus(OfferRequest request, Long realEstateId, Long offerId);

    void chooseOfferCategory(OfferSpec offerSpec, Authentication authentication);
}
