package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.model.Offer;

public interface OfferFactory {

	Offer createOffer(OfferRequest request, Long realEstateId);
}
