package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.CadastralDataNotOwnedByRealEstateException;
import com.dietiestates.resource_server.verifier.CadastralDataVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralDataVerifierDefaultImpl implements CadastralDataVerifier {

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
