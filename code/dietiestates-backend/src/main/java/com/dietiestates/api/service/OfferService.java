package com.dietiestates.api.service;

import org.springframework.security.core.Authentication;

import com.dietiestates.api.dto.request.CounterOfferRequest;
import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;

public interface OfferService {

	OfferResponse createOffer(OfferRequest request, Long realEstateId);

	OfferResponse getOfferById(Long realEstateId, Long offerId);

	OfferResponse acceptOffer(Long id, Authentication auth);

	OfferResponse rejectOffer(Long id, Authentication auth);

	OfferResponse counterOffer(Long id, CounterOfferRequest req, Authentication auth);
}
