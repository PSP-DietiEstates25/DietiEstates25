package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;

public interface NegotiationVerifier {
    Boolean checkNegotiationAlreadyExists(String userEmail, Long realEstateId);
}
