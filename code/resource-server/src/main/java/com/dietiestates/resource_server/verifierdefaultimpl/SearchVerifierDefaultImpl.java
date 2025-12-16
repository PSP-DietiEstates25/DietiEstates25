package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.SearchNotOwnedByUserException;
import com.dietiestates.resource_server.repository.SearchRepository;
import com.dietiestates.resource_server.verifier.SearchVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchVerifierDefaultImpl implements SearchVerifier {

    private final SearchRepository searchRepository;

    @Override
    public void checkSearchOwnedByUser(Long id, String userEmail) throws SearchNotOwnedByUserException {
        if(!searchRepository.existsByIdAndUserEmail(id, userEmail))
            throw new SearchNotOwnedByUserException();
    }
}
