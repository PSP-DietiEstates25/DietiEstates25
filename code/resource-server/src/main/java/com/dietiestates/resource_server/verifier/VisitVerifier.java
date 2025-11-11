package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitVerifier {
    void checkVisitExists(Long visitId, Long negotiationId) throws VisitNotFoundException;
    void checkVisitOwnedByRealEstate(Long id, Long realEstateId) throws VisitNotOwnedByRealEstateException;
}
