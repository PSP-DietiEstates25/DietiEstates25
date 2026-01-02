package com.dietiestates.resource_server.verifier;

public interface NegotiationVerifier {
    Boolean checkNegotiationAlreadyExists(String userEmail, Long realEstateId);
}
