package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.GeographicalPositionNotOwnedByDetailException;
import com.dietiestates.resource_server.verifier.GeographicalPositionVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeographicalPositionVerifierDefaultImpl implements GeographicalPositionVerifier {

	public void checkGeographicalPositionOwnedByDetail(
			Long geographicalPositionDetailId,
			Long detailId
			)
		throws GeographicalPositionNotOwnedByDetailException {
		
		if(!geographicalPositionDetailId.equals(detailId))
			throw new GeographicalPositionNotOwnedByDetailException();
	}
}
