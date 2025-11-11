package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.factory.NegotiationFactory;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.repository.NegotiationRepository;
import com.dietiestates.resource_server.service.NegotiationService;
import com.dietiestates.resource_server.spec.NegotiationSpec;
import com.dietiestates.resource_server.verifier.NegotiationVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NegotiationServiceDefaultImpl implements NegotiationService {

    private final NegotiationFactory negotiationFactory;
    private final NegotiationFinder negotiationFinder;
    private final NegotiationRepository negotiationRepository;
    private final NegotiationVerifier negotiationVerifier;

    private final UserFinder userFinder;
    private final EstateAgentFinder estateAgentFinder;
    private final RealEstateFinder realEstateFinder;

    @Override
    public Negotiation setupNegotiation(NegotiationSpec negotiationSpec) {
        if(!negotiationVerifier.checkNegotiationAlreadyExists(negotiationSpec.getUserEmail(), negotiationSpec.getRealEstateId()))
            return createNegotiation(negotiationSpec);
        else return getNegotiationByUserAndRealEstate(negotiationSpec);
    }

    @Override
    public Negotiation createNegotiation(NegotiationSpec negotiationSpec){

        var user = userFinder.getUserByEmail(negotiationSpec.getUserEmail());
        var estateAgent = estateAgentFinder.getEstateAgentByEmail(negotiationSpec.getEstateAgentEmail());
        var realEstate = realEstateFinder.getRealEstateById(negotiationSpec.getRealEstateId());

        var negotiation = negotiationFactory.createNegotiationFromSpec(user, estateAgent, realEstate);
        negotiationRepository.save(negotiation);

        return negotiation;
    }

    @Override
    public Negotiation getNegotiationByUserAndRealEstate(NegotiationSpec negotiationSpec) {

        var user = userFinder.getUserByEmail(negotiationSpec.getUserEmail());
        var realEstate = realEstateFinder.getRealEstateById(negotiationSpec.getRealEstateId());

        return negotiationFinder.getNegotiationByUserAndRealEstate(user, realEstate);
    }

}
