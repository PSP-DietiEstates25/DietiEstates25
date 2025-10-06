package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.api.verifier.VisitVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
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
