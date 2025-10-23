package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.GeographicalPositionNotOwnedByDetailException;

public interface GeographicalPositionVerifier {

	void checkGeographicalPositionOwnedByDetail(
			Long geographicalPositionDetailId,
			Long detailId
			)
		throws GeographicalPositionNotOwnedByDetailException;
}
