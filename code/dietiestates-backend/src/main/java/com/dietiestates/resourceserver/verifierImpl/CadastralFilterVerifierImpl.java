package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.CadastralFilterNotOwnedBySearchException;
import com.dietiestates.resourceserver.verifier.CadastralFilterVerifier;

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
