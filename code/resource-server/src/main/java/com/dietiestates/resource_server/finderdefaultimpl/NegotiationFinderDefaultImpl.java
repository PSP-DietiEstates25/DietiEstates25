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
    public Negotiation getRealEstateUserNegotiation(Long realEstateId, Long userId) throws NegotiationNotFoundException {
        return negotiationRepository.findByRealEstateIdAndUserId(realEstateId, userId)
                .orElseThrow(NegotiationNotFoundException::new);
    }

    @Override
    public Negotiation getRealEstateEstateAgentNegotiation(Long realEstataId, Long userId) throws NegotiationNotFoundException {
        return negotiationRepository.findByRealEstateIdAndEstateAgentId(realEstataId, userId)
                .orElseThrow(NegotiationNotFoundException::new);
    }
}
