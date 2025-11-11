package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.VisitFinder;
import com.dietiestates.resource_server.repository.VisitRepository;
import com.dietiestates.resource_server.verifier.VisitVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitVerifierDefaultImpl implements VisitVerifier {

    private final VisitRepository visitRepository;
    private final VisitFinder visitFinder;
    private final RealEstateFinder realEstateFinder;

    @Override
    public void checkVisitExists(Long visitId, Long negotiationId) throws VisitNotFoundException {
        if(!visitRepository.existsByIdAndNegotiationId(visitId, negotiationId))
            throw new VisitNotFoundException();
    }

    @Override
	public void checkVisitOwnedByRealEstate(Long visitId, Long realEstateId) throws VisitNotOwnedByRealEstateException {

        var visit = visitFinder.getVisitById(visitId);
        var negotiation = visit.getNegotiation();
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        if(!negotiation.getRealEstate().getId().equals(realEstate.getId()))
            throw new VisitNotOwnedByRealEstateException();
	}
}
