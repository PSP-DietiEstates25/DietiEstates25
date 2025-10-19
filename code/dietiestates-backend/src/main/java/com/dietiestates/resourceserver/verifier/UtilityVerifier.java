package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.UtilityNotOwnedByDetailException;

public interface UtilityVerifier {

	void checkUtilityOwnedByDetail(Long utilityDetailId, Long detailId)
		throws UtilityNotOwnedByDetailException;
}
