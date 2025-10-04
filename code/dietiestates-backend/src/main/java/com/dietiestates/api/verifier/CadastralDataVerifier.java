package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.CadastralDataNotOwnedByRealEstateException;

public interface CadastralDataVerifier {

	void checkCadastralDataOwnedByRealEstate(
			Long cadastralDataRealEstateId,
			Long realEstateId
			)
			throws CadastralDataNotOwnedByRealEstateException;
}
