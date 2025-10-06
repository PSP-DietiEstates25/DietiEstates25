package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;

public interface OfferService {

	void createOffer(OfferRequest request, Long realEstateId);
	
	OfferResponse getOfferById(Long realEstateId, Long offerId);
}
