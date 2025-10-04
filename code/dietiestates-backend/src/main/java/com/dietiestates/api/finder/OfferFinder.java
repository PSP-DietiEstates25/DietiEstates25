package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.OfferNotFoundException;
import com.dietiestates.api.model.Offer;

public interface OfferFinder {

	Offer getOfferById(Long id)
			throws OfferNotFoundException;
}
