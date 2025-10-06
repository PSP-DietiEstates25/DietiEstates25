package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitVerifier {

	void checkVisitOwnedByRealEstate(
			Long visitRealEstateId,
			Long realEstateId
			)
		throws VisitNotOwnedByRealEstateException;
}
