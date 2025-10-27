package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.CadastralFilterNotOwnedBySearchException;

public interface CadastralFilterVerifier {

	void checkCadastralFilterOwnedBySearch(
			Long cadastralFilterSearchId,
			Long searchId
			)
		throws CadastralFilterNotOwnedBySearchException;
}
