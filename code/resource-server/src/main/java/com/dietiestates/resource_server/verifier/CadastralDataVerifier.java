package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.CadastralDataNotOwnedByRealEstateException;

public interface CadastralDataVerifier {

	void checkCadastralDataOwnedByRealEstate(
			Long cadastralDataRealEstateId,
			Long realEstateId
			)
			throws CadastralDataNotOwnedByRealEstateException;
}
