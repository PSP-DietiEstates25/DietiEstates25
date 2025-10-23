package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.OfferNotOwnedByRealEstateException;

public interface OfferVerifier {

	void checkOfferOwnedByRealEstate(
			Long realEstateOfferId,
			Long offerId
			)
		throws OfferNotOwnedByRealEstateException;
}
