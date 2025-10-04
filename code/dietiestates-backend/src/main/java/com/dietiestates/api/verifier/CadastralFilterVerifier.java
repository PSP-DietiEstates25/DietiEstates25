package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.CadastralFilterNotOwnedBySearchException;

public interface CadastralFilterVerifier {

	void checkCadastralFilterOwnedBySearch(
			Long cadastralFilterSearchId,
			Long searchId
			)
		throws CadastralFilterNotOwnedBySearchException;
}
