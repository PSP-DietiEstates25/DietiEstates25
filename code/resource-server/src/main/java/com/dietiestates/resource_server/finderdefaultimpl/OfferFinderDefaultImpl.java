package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.finder.OfferFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.repository.OfferRepository;
import com.dietiestates.resource_server.utils.PageUtils;
import com.dietiestates.resource_server.utils.ProposalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @Override
    public Page<Offer> getAllUserOffers(Long userId, String status, Pageable pageable){
        List<Negotiation> allUserNegotiations = negotiationFinder.getAllUserNegotiations(userId);
        List<Offer> allNegotiationsOffers = extractAllNegotiationsOffers(allUserNegotiations, status);
        allNegotiationsOffers.sort(Comparator.comparing(Offer::getCreatedDate).reversed());
        return PageUtils.toPage(allNegotiationsOffers, pageable);
    }

    @Override
    public Page<Offer> getAllEstateAgentOffers(Long estateAgentId, String status, Pageable pageable){
        List<Negotiation> allEstateAgentNegotiations = negotiationFinder.getAllEstateAgentNegotiations(estateAgentId);
        List<Offer> allNegotiationOffers = extractAllNegotiationsOffers(allEstateAgentNegotiations, status);
        allNegotiationOffers.sort(Comparator.comparing(Offer::getCreatedDate).reversed());
        return PageUtils.toPage(allNegotiationOffers, pageable);
    }

    @Override
    public List<Offer> extractAllNegotiationsOffers(List<Negotiation> negotiations, String status){
        var offers = new ArrayList<Offer>();
        ProposalStatus requestedStatus = null;

        if(ProposalUtils.checkProposalStatusExists(status)){
            requestedStatus = ProposalUtils.extractProposalStatus(status);
        }

        var targetStatus = requestedStatus;

        negotiations.forEach(negotiation -> {
            var negotiationOffers = negotiation.getOffers();
            if(targetStatus != null){
                negotiationOffers.forEach(offer -> {
                    if(targetStatus.equals(offer.getProposalStatus()))
                        offers.add(offer);
                });
            } else {
                offers.addAll(negotiationOffers);
            }
        });
        return offers;
    }
}
