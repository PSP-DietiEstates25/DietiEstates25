package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resourceserver.verifier.VisitVerifier;

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
