package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.repository.OfferRepository;
import com.dietiestates.resource_server.verifier.OfferVerifier;
import com.dietiestates.resource_server.finder.OfferFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferVerifierDefaultImpl implements OfferVerifier {

    private final OfferRepository offerRepository;
    private final OfferFinder offerFinder;
    private final RealEstateFinder realEstateFinder;

    @Override
    public void checkOfferExists(Long offerId, Long negotiationId) throws OfferNotFoundException {
        if(!offerRepository.existsByIdAndNegotiationId(offerId, negotiationId))
            throw new OfferNotFoundException();
    }

    @Override
    public void checkOfferOwnedByRealEstate(Long offerId, Long realEstateId) throws OfferNotOwnedByRealEstateException {

        var offer = offerFinder.getOfferById(offerId);
        var negotiation = offer.getNegotiation();
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        if(!negotiation.getRealEstate().getId().equals(realEstate.getId()))
            throw new OfferNotOwnedByRealEstateException();
    }
}