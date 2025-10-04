package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.UtilityNotOwnedByDetailException;
import com.dietiestates.api.verifier.UtilityVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UtilityVerifierImpl implements UtilityVerifier {

	@Override
	public void checkUtilityOwnedByDetail(Long utilityDetailId, Long detailId) throws UtilityNotOwnedByDetailException {
		if(!utilityDetailId.equals(detailId))
			throw new UtilityNotOwnedByDetailException();
	}

}
