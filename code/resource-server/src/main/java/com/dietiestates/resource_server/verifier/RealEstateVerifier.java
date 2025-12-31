package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resource_server.exception.notowned.RealEstateNotOwnedByAdminException;
import com.dietiestates.resource_server.exception.notowned.RealEstateNotOwnedByEstateAgentException;

public interface RealEstateVerifier {
    void checkRealEstateExists(Long id) throws RealEstateNotFoundException;
    void checkRealEstateOwnedByEstateAgent(Long realEstateId, String estateAgentEmail) throws RealEstateNotOwnedByEstateAgentException;

    void checkRealEstateOwnedByAdmin(Long realEstateId, String adminEmail) throws RealEstateNotOwnedByAdminException;
}
