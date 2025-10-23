package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.OfferRequest;
import com.dietiestates.resourceserver.dto.response.OfferResponse;

public interface OfferService {

	OfferResponse createOffer(OfferRequest request, Long realEstateId);
	
	OfferResponse getOfferById(Long realEstateId, Long offerId);
}
