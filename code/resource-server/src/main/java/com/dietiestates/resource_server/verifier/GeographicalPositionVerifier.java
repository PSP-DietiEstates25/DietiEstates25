package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.GeographicalPositionNotOwnedByDetailException;

public interface GeographicalPositionVerifier {

	void checkGeographicalPositionOwnedByDetail(
			Long geographicalPositionDetailId,
			Long detailId
			)
		throws GeographicalPositionNotOwnedByDetailException;
}
