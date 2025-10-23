package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.GeographicalPositionNotOwnedByDetailException;
import com.dietiestates.resourceserver.verifier.GeographicalPositionVerifier;

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
