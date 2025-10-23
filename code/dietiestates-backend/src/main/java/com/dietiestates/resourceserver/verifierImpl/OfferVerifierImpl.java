package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resourceserver.verifier.OfferVerifier;

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
