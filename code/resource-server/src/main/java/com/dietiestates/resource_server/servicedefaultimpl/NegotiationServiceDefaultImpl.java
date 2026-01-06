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
        // === EXTERNAL ===
        if (negotiationSpec.getUserEmail() == null || negotiationSpec.getUserEmail().isBlank()) {
            if (!negotiationVerifier.checkNegotiationAlreadyExists(null, negotiationSpec.getRealEstateId(), negotiationSpec.getEstateAgentEmail())) {
                return createNegotiation(negotiationSpec); // createNegotiation deve essere null-safe (vedi sotto)
            }
            return negotiationFinder.getActiveExternalNegotiation(
                    negotiationSpec.getRealEstateId(),
                    estateAgentFinder.getEstateAgentByEmail(negotiationSpec.getEstateAgentEmail()).getId()
            );
        }

        // === USER ===
        if (!negotiationVerifier.checkNegotiationAlreadyExists(negotiationSpec.getUserEmail(), negotiationSpec.getRealEstateId(), negotiationSpec.getEstateAgentEmail())) {
            return createNegotiation(negotiationSpec);
        }
        return getNegotiationByUserAndRealEstate(negotiationSpec);
    }

    @Override
    public Negotiation createNegotiation(NegotiationSpec negotiationSpec){

        var estateAgent = estateAgentFinder.getEstateAgentByEmail(negotiationSpec.getEstateAgentEmail());
        var realEstate = realEstateFinder.getRealEstateById(negotiationSpec.getRealEstateId());

        var user = (negotiationSpec.getUserEmail() == null || negotiationSpec.getUserEmail().isBlank())
                ? null
                : userFinder.getUserByEmail(negotiationSpec.getUserEmail());


        var negotiation = negotiationFactory.createNegotiationFromSpec(user, estateAgent, realEstate);
        negotiationRepository.save(negotiation);

        return negotiation;
    }

    @Override
    public Negotiation getNegotiationByUserAndRealEstate(NegotiationSpec negotiationSpec) {
        var user = userFinder.getUserByEmail(negotiationSpec.getUserEmail());
        return negotiationFinder.getRealEstateUserNegotiation(negotiationSpec.getRealEstateId(), user.getId());
    }

}
