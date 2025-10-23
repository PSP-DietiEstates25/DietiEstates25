package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitVerifier {

	void checkVisitOwnedByRealEstate(
			Long visitRealEstateId,
			Long realEstateId
			)
		throws VisitNotOwnedByRealEstateException;
}
