package com.dietiestates.api.verifierImpl;

import com.dietiestates.api.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.api.verifier.VisitVerifier;

public class VisitVerifierImpl implements VisitVerifier {

	@Override
	public void checkVisitOwnedByRealEstate(
			Long visitRealEstateId,
			Long realEstateId
			)
					throws VisitNotOwnedByRealEstateException {
		if(!visitRealEstateId.equals(realEstateId))
			throw new VisitNotOwnedByRealEstateException();
	}

}
