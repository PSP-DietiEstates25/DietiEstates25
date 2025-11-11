package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.NegotiationNotFoundException;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NegotiationFinderDefaultImpl implements NegotiationFinder {

    private final NegotiationRepository negotiationRepository;

    public Negotiation getNegotiationById(Long id){
        return negotiationRepository.findById(id)
                .orElseThrow(NegotiationNotFoundException::new);
    }

    @Override
    public Negotiation getNegotiationByRealEstate(RealEstate realEstate) throws NegotiationNotFoundException {
        return negotiationRepository.findByRealEstate(realEstate)
                .orElseThrow(NegotiationNotFoundException::new);
    }

    @Override
    public Negotiation getNegotiationByUserAndRealEstate(User user, RealEstate realEstate) throws NegotiationNotFoundException {
        return negotiationRepository.findByUserAndRealEstate(user, realEstate)
                .orElseThrow(NegotiationNotFoundException::new);
    }

    @Override
    public Negotiation getNegotiationByEstateAgentAndRealEstate(EstateAgent estateAgent, RealEstate realEstate) throws NegotiationNotFoundException {
        return negotiationRepository.findByEstateAgentAndRealEstate(estateAgent, realEstate)
                .orElseThrow(NegotiationNotFoundException::new);
    }
}
