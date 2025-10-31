package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.spec.OfferSpec;

public interface OfferMapper {

	OfferSpec toSpec(OfferRequest request);

	OfferResponse fromEntity(Offer offer);

	OfferResponse toResponse(Offer offer);
}
