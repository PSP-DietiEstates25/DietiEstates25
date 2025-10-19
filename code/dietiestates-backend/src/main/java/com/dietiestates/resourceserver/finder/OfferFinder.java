package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.OfferNotFoundException;
import com.dietiestates.resourceserver.model.Offer;

public interface OfferFinder {

	Offer getOfferById(Long id)
			throws OfferNotFoundException;
}
