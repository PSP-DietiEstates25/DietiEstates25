package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.CadastralFilterNotOwnedBySearchException;
import com.dietiestates.api.verifier.CadastralFilterVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralFilterVerifierImpl implements CadastralFilterVerifier {

	@Override
	public void checkCadastralFilterOwnedBySearch(Long cadastralFilterSearchId, Long searchId)
			throws CadastralFilterNotOwnedBySearchException {
		if(!cadastralFilterSearchId.equals(searchId))
			throw new CadastralFilterNotOwnedBySearchException();
	}

}
