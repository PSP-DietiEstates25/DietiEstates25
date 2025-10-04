package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.GeographicalPositionNotOwnedByDetailException;

public interface GeographicalPositionVerifier {

	void checkGeographicalPositionOwnedByDetail(
			Long geographicalPositionDetailId,
			Long detailId
			)
		throws GeographicalPositionNotOwnedByDetailException;
}
