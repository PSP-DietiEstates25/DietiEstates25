package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resource_server.verifier.VisitVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitVerifierDefaultImpl implements VisitVerifier {

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
