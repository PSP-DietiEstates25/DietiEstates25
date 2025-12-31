package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.finder.VisitFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.repository.VisitRepository;
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
public class VisitFinderDefaultImpl implements VisitFinder {

	private final VisitRepository visitRepository;
    private final NegotiationFinder negotiationFinder;

	@Override
	public Visit getVisitById(Long id) throws VisitNotFoundException {
		return visitRepository.findById(id)
				.orElseThrow(VisitNotFoundException::new);
	}

	@Override
	public Page<Visit> getRealEstateVisits(Long realEstateId, Pageable pageable) {
		return visitRepository.findActiveByRealEstateId(realEstateId, pageable);
	}

    @Override
    public Page<Visit> getAllEstateAgentVisits(Long estateAgentId, String status, Pageable pageable) {
        List<Negotiation> allEstateAgentNegotiations = negotiationFinder.getAllEstateAgentNegotiationsForActiveRealEstates(estateAgentId);
        List<Visit> allNegotiationVisits = extractAllNegotiationsVisits(allEstateAgentNegotiations, status);
        allNegotiationVisits.sort(Comparator.comparing(Visit::getCreatedDate).reversed());
        return PageUtils.toPage(allNegotiationVisits, pageable);
    }

    @Override
    public List<Visit> extractAllNegotiationsVisits(List<Negotiation> negotiations, String status){
        var visits = new ArrayList<Visit>();
        ProposalStatus requestedStatus = null;

        if(ProposalUtils.checkProposalStatusExists(status)){
            requestedStatus = ProposalUtils.extractProposalStatus(status);
        }

        var targetStatus = requestedStatus;

        negotiations.forEach(negotiation -> {
            var negotiationOffers = negotiation.getVisits();
            if(targetStatus != null){
                negotiationOffers.forEach(visit -> {
                    if(targetStatus.equals(visit.getProposalStatus()))
                        visits.add(visit);
                });
            } else {
                visits.addAll(negotiationOffers);
            }
        });
        return visits;
    }
}
