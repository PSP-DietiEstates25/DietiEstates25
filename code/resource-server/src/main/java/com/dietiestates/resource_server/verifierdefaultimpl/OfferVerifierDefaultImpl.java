package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resource_server.repository.OfferRepository;
import com.dietiestates.resource_server.verifier.OfferVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferVerifierDefaultImpl implements OfferVerifier {

    private final OfferRepository offerRepository;

    @Override
    public void checkOfferExists(Long id) throws OfferNotFoundException {
        if(!offerRepository.existsById(id))
            throw new OfferNotFoundException();
    }

    @Override
    public void checkOfferOwnedByRealEstate(Long id, Long realEstateId) throws OfferNotOwnedByRealEstateException {
        if(!offerRepository.existsByIdAndRealEstateId(id, realEstateId))
            throw new OfferNotOwnedByRealEstateException();
    }
}
