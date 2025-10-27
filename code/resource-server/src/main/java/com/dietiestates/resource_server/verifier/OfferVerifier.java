package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;

public interface OfferVerifier {

	void checkOfferOwnedByRealEstate(
			Long realEstateOfferId,
			Long offerId
			)
		throws OfferNotOwnedByRealEstateException;
}
