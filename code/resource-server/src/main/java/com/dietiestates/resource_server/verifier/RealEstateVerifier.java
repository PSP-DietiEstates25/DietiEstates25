package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;

public interface RealEstateVerifier {

    void checkRealEstateExists(Long id) throws RealEstateNotFoundException;
}
