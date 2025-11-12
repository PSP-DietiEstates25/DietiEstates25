package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.finder.OfferFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.repository.OfferRepository;
import com.dietiestates.resource_server.verifier.RealEstateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferFinderDefaultImpl implements OfferFinder {

	private final OfferRepository offerRepository;
    private final NegotiationFinder negotiationFinder;

	@Override
	public Offer getOfferById(Long id) throws OfferNotFoundException {
		return offerRepository.findById(id)
				.orElseThrow(OfferNotFoundException::new);
	}

    @Override
    public Page<Offer> getRealEstateUserOffers(Long realEstateId, Long userId, Pageable pageable) {
        var negotiation = negotiationFinder.getRealEstateUserNegotiation(realEstateId, userId);
        return offerRepository.findByNegotiationId(negotiation.getId(), pageable);
    }

    @Override
    public Page<Offer> getRealEstateEstateAgentOffers(Long realEstateId, Long estateAgentId, Pageable pageable) {
        var negotiation = negotiationFinder.getRealEstateEstateAgentNegotiation(realEstateId, estateAgentId);
        return offerRepository.findByNegotiationId(negotiation.getId(), pageable);
    }

}
