package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.UtilityNotOwnedByDetailException;
import com.dietiestates.resource_server.verifier.UtilityVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilityVerifierDefaultImpl implements UtilityVerifier {

	@Override
	public void checkUtilityOwnedByDetail(Long utilityDetailId, Long detailId) throws UtilityNotOwnedByDetailException {
		if(!utilityDetailId.equals(detailId))
			throw new UtilityNotOwnedByDetailException();
	}

}
