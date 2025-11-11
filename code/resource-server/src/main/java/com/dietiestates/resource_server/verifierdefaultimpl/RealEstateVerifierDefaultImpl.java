package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resource_server.exception.notowned.RealEstateNotOwnedByEstateAgentException;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import com.dietiestates.resource_server.verifier.RealEstateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RealEstateVerifierDefaultImpl implements RealEstateVerifier {

    private final RealEstateRepository realEstateRepository;

    @Override
    public void checkRealEstateExists(Long id) throws RealEstateNotFoundException {
        if(!realEstateRepository.existsById(id))
            throw new RealEstateNotFoundException();
    }

    @Override
    public void checkRealEstateOwnedByEstateAgent(Long realEstateId, String estateAgentEmail) throws RealEstateNotOwnedByEstateAgentException {
        if(!realEstateRepository.existsByIdAndEstateAgentEmail(realEstateId, estateAgentEmail))
            throw new RealEstateNotOwnedByEstateAgentException();
    }
}
