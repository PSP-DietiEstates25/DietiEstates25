package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.CadastralDataNotOwnedByRealEstateException;

public interface CadastralDataVerifier {

	void checkCadastralDataOwnedByRealEstate(
			Long cadastralDataRealEstateId,
			Long realEstateId
			)
			throws CadastralDataNotOwnedByRealEstateException;
}
