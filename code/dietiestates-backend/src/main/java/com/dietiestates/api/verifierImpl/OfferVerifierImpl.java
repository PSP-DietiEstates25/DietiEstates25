package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.api.verifier.OfferVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfferVerifierImpl implements OfferVerifier {

	@Override
	public void checkOfferOwnedByRealEstate(Long realEstateOfferId, Long offerId)
			throws OfferNotOwnedByRealEstateException {
		// TODO Auto-generated method stub
		
	}

}
