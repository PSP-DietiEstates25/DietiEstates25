package com.dietiestates.resource_server.verifier;

public interface UserVerifier {
    Boolean checkUserAlreadyExists(String userEmail);
}
