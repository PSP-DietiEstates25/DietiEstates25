package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.UtilityNotOwnedByDetailException;

public interface UtilityVerifier {

	void checkUtilityOwnedByDetail(Long utilityDetailId, Long detailId)
		throws UtilityNotOwnedByDetailException;
}
