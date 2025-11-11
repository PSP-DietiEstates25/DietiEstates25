package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;

public interface OfferVerifier {
    void checkOfferExists(Long offerId, Long negotiationId) throws OfferNotFoundException;
    void checkOfferOwnedByRealEstate(Long offerId, Long realEstateId) throws OfferNotOwnedByRealEstateException;
}
