package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.OfferNotOwnedByRealEstateException;

public interface OfferVerifier {

	void checkOfferOwnedByRealEstate(
			Long realEstateOfferId,
			Long offerId
			)
		throws OfferNotOwnedByRealEstateException;
}
