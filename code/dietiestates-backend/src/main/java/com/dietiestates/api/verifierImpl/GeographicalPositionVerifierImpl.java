package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.GeographicalPositionNotOwnedByDetailException;
import com.dietiestates.api.verifier.GeographicalPositionVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeographicalPositionVerifierImpl implements GeographicalPositionVerifier {

	public void checkGeographicalPositionOwnedByDetail(
			Long geographicalPositionDetailId,
			Long detailId
			)
		throws GeographicalPositionNotOwnedByDetailException {
		
		if(!geographicalPositionDetailId.equals(detailId))
			throw new GeographicalPositionNotOwnedByDetailException();
	}
}
