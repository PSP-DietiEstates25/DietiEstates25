package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.spec.OfferSpec;

public interface OfferMapper {

	OfferSpec toSpec(OfferRequest request);
	
	OfferResponse fromEntity(Offer offer);
}
