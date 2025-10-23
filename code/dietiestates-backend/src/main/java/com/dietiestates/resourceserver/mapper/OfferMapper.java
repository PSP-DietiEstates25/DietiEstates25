package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.OfferRequest;
import com.dietiestates.resourceserver.dto.response.OfferResponse;
import com.dietiestates.resourceserver.model.Offer;
import com.dietiestates.resourceserver.spec.OfferSpec;

public interface OfferMapper {

	OfferSpec toSpec(OfferRequest request);
	
	OfferResponse fromEntity(Offer offer);
}
