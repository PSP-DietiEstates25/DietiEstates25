package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;

public interface OfferVerifier {

    void checkOfferExists(Long id) throws OfferNotFoundException;

    void checkOfferOwnedByRealEstate(Long id, Long realEstateId) throws OfferNotOwnedByRealEstateException;
}
