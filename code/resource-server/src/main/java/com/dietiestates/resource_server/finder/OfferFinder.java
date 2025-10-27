package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.model.Offer;

public interface OfferFinder {

	Offer getOfferById(Long id)
			throws OfferNotFoundException;
}
