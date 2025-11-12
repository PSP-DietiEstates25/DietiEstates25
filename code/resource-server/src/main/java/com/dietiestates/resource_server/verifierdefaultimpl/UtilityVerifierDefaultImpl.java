package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.UtilityNotOwnedByDetailException;
import com.dietiestates.resource_server.repository.DetailRepository;
import com.dietiestates.resource_server.verifier.UtilityVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilityVerifierDefaultImpl implements UtilityVerifier {

    private final DetailRepository detailRepository;

    @Override
    public void checkUtilityOwnedByDetail(Long utilityId) throws UtilityNotOwnedByDetailException {
        if(!detailRepository.existsByUtilityId(utilityId))
            throw new UtilityNotOwnedByDetailException();
    }
}
