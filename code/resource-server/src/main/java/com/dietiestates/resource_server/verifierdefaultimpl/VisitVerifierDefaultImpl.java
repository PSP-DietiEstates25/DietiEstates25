package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resource_server.repository.VisitRepository;
import com.dietiestates.resource_server.verifier.VisitVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitVerifierDefaultImpl implements VisitVerifier {

    private final VisitRepository visitRepository;

    @Override
    public void checkVisitExists(Long id) throws VisitNotFoundException {
        if(!visitRepository.existsById(id))
            throw new VisitNotFoundException();
    }

    @Override
	public void checkVisitOwnedByRealEstate(Long id, Long realEstateId) throws VisitNotOwnedByRealEstateException {
		if(!visitRepository.existsByIdAndRealEstateId(id, realEstateId))
			throw new VisitNotOwnedByRealEstateException();
	}

}
