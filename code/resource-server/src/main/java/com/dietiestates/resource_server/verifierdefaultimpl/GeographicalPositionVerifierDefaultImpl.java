package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.GeographicalPositionNotOwnedByDetailException;
import com.dietiestates.resource_server.repository.DetailRepository;
import com.dietiestates.resource_server.verifier.GeographicalPositionVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeographicalPositionVerifierDefaultImpl implements GeographicalPositionVerifier {

    private final DetailRepository detailRepository;

    @Override
    public void checkGeographicalPositionOwnedByDetail(Long geographicalPositionId) throws GeographicalPositionNotOwnedByDetailException {
        if(!detailRepository.existsByGeographicalPositionId(geographicalPositionId))
            throw new GeographicalPositionNotOwnedByDetailException();
    }
}
