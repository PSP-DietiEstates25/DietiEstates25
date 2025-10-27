package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resource_server.verifier.OfferVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferVerifierDefaultImpl implements OfferVerifier {

	@Override
	public void checkOfferOwnedByRealEstate(Long realEstateOfferId, Long offerId)
			throws OfferNotOwnedByRealEstateException {
		// TODO Auto-generated method stub
		
	}

}
