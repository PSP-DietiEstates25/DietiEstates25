package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.CadastralDataNotOwnedByRealEstateException;
import com.dietiestates.api.verifier.CadastralDataVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralDataVerifierImpl implements CadastralDataVerifier {

	@Override
	public void checkCadastralDataOwnedByRealEstate(
			Long cadastralDataRealEstateId,
			Long realEstateId
			)
			throws CadastralDataNotOwnedByRealEstateException {
		if(!cadastralDataRealEstateId.equals(realEstateId))
			 throw new CadastralDataNotOwnedByRealEstateException();		
	}
	
}
