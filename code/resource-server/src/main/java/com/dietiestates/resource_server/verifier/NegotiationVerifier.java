package com.dietiestates.resource_server.verifier;

public interface NegotiationVerifier {
    boolean checkNegotiationAlreadyExists(String userEmail, Long realEstateId, String estateAgentEmail);
}
