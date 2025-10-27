package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitVerifier {

	void checkVisitOwnedByRealEstate(
			Long visitRealEstateId,
			Long realEstateId
			)
		throws VisitNotOwnedByRealEstateException;
}
