package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.SearchNotOwnedByUserException;

public interface SearchVerifier {
    void checkSearchOwnedByUser(Long id, String userEmail) throws SearchNotOwnedByUserException;
}
