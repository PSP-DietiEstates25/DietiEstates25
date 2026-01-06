package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.NegotiationNotFoundException;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return negotiationRepository.findActiveByRealEstateIdAndUserId(realEstateId, userId)
                .orElseThrow(NegotiationNotFoundException::new);
    }

    @Override
    public Negotiation getRealEstateEstateAgentNegotiation(Long realEstataId, Long estateAgentId) throws NegotiationNotFoundException {
        return negotiationRepository.findActiveByRealEstateIdAndEstateAgentId(realEstataId, estateAgentId)
                .orElseThrow(NegotiationNotFoundException::new);
    }

    @Override
    public List<Negotiation> getAllRealEstateEstateAgentNegotiationsForActiveRealEstate(Long realEstateId, Long estateAgentId) {
        return negotiationRepository.findAllActiveByRealEstateIdAndEstateAgentId(realEstateId, estateAgentId);
    }

    @Override
    public List<Negotiation> getAllUserNegotiationsForActiveRealEstates(Long userId){
        return negotiationRepository.findByUserIdAndActiveRealEstates(userId);
    }

    @Override
    public List<Negotiation> getAllUserNegotiationsForAllRealEstates(Long userId){
        return negotiationRepository.findByUserIdAndAllRealEstates(userId);
    }

    @Override
    public List<Negotiation> getAllEstateAgentNegotiationsForActiveRealEstates(Long estateAgentId){
        return negotiationRepository.findByEstateAgentIdAndActiveRealEstates(estateAgentId);
    }

    @Override
    public List<Negotiation> getAllEstateAgentNegotiationsForAllRealEstates(Long estateAgentId){
        return negotiationRepository.findByEstateAgentIdAndAllRealEstates(estateAgentId);
    }
}
