package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.CadastralFilterNotOwnedBySearchException;

public interface CadastralFilterVerifier {

	void checkCadastralFilterOwnedBySearch(
			Long cadastralFilterSearchId,
			Long searchId
			)
		throws CadastralFilterNotOwnedBySearchException;
}
