package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.UtilityNotOwnedByDetailException;
import com.dietiestates.resourceserver.verifier.UtilityVerifier;

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
