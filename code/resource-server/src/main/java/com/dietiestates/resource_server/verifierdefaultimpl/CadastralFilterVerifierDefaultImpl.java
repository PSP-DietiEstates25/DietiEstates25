package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.CadastralFilterNotOwnedBySearchException;
import com.dietiestates.resource_server.verifier.CadastralFilterVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralFilterVerifierDefaultImpl implements CadastralFilterVerifier {

	@Override
	public void checkCadastralFilterOwnedBySearch(Long cadastralFilterSearchId, Long searchId)
			throws CadastralFilterNotOwnedBySearchException {
		if(!cadastralFilterSearchId.equals(searchId))
			throw new CadastralFilterNotOwnedBySearchException();
	}

}
