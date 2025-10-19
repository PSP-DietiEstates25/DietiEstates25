package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.CadastralDataNotOwnedByRealEstateException;
import com.dietiestates.resourceserver.verifier.CadastralDataVerifier;

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
