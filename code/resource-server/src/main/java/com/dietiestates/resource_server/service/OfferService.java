package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;

public interface OfferService {

	OfferResponse createOffer(OfferRequest request, Long realEstateId);
	
	OfferResponse getOfferById(Long realEstateId, Long offerId);
}
