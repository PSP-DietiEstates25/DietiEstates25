package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resource_server.factory.VisitFactory;
import com.dietiestates.resource_server.finder.*;
import com.dietiestates.resource_server.mapper.VisitMapper;
import com.dietiestates.resource_server.repository.VisitRepository;
import com.dietiestates.resource_server.service.NegotiationService;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.service.VisitService;
import com.dietiestates.resource_server.spec.NegotiationSpec;
import com.dietiestates.resource_server.verifier.RealEstateVerifier;
import com.dietiestates.resource_server.verifier.VisitVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitServiceDefaultImpl implements VisitService {
	
	private final VisitRepository visitRepository;
	private final VisitFactory visitFactory;
	private final VisitFinder visitFinder;
	private final VisitVerifier visitVerifier;
	private final VisitMapper visitMapper;

    private final NotificationService notificationService;
    private final NegotiationService negotiationService;
    private final RealEstateFinder realEstateFinder;
    private final EstateAgentFinder estateAgentFinder;

	@Override
	public VisitResponse createVisit(VisitRequest request, Long realEstateId, String userEmail) {

        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

		var visitSpec = visitMapper.toSpec(request);
        var negotiationSpec = NegotiationSpec.builder()
                .userEmail(userEmail)
                .estateAgentEmail(realEstate.getEstateAgent().getEmail())
                .realEstateId(realEstateId)
                .build();

        var negotiation = negotiationService.setupNegotiation(negotiationSpec);
		var visit = visitFactory.createVisitFromSpec(visitSpec, negotiation);

		visitRepository.save(visit);
		
		return visitMapper.fromEntity(visit);
	}

	@Override
	public VisitResponse getVisitById(Long realEstateId, Long visitId) throws VisitNotOwnedByRealEstateException {
        visitVerifier.checkVisitOwnedByRealEstate(visitId, realEstateId);
		var visit = visitFinder.getVisitById(visitId);

		return visitMapper.fromEntity(visit);
	}

    @Override
    public Page<VisitResponse> getRealEstateVisits(Long realEstateId, Integer page, Integer size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var realEstateVisits = visitFinder.getRealEstateVisits(realEstateId, pageable);

        return visitMapper.createPagedVisitsResponse(realEstateVisits);
    }

    @Override
    public Page<VisitResponse> getAllEstateAgentVisits(String estateAgentEmail, String status, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
        var visits = visitFinder.getAllEstateAgentVisits(estateAgent.getId(), status, pageable);
        return visitMapper.createPagedVisitsResponse(visits);
    }

    @Override
    public VisitResponse updateVisitStatus(VisitRequest request, Long realEstateId, Long visitId) throws VisitNotOwnedByRealEstateException {

        visitVerifier.checkVisitOwnedByRealEstate(visitId, realEstateId);

        var visitSpec = visitMapper.toSpec(request);
        var visitToUpdate = visitFinder.getVisitById(visitId);
        var negotiation = visitToUpdate.getNegotiation();
        var user = negotiation.getUser();
        visitToUpdate.setProposalStatus(ProposalStatus.valueOf(visitSpec.getStatus()));

        createVisitNotification(visitToUpdate.getProposalStatus(), user.getEmail());

        visitRepository.save(visitToUpdate);
        return visitMapper.fromEntity(visitToUpdate);
    }

    public void createVisitNotification(
            ProposalStatus proposalStatus,
            String userEmail
    ){

        var message = (proposalStatus.equals(ProposalStatus.ACCEPTED)) ? "Visit accepted"
                : "Visit rejected";

        notificationService.createNotification(
                NotificationCategoryType.VISIT.toString(),
                NotificationRequest.builder()
                        .message(message)
                        .build()
        );
    }

    /*
    @Transactional
    public VisitResponse acceptVisit(Long id, Authentication auth) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + id));

        visit.setProposalStatus(ProposalStatus.ACCEPTED);
        var saved = visitRepository.save(visit);

        notificationService.createNotification(
                "VISIT",
                NotificationRequest.builder()
                        .message("Visita accettata")
                        .userEmail(saved.getUser().getSecurityAccountDecorator().getAccountEmail())
                        .build());

        return visitMapper.toResponse(saved);
    }

    @Transactional
    public VisitResponse rejectVisit(Long id, Authentication auth) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + id));

        visit.setProposalStatus(ProposalStatus.REJECTED);
        var saved = visitRepository.save(visit);

        notificationService.createNotification(
                "VISIT",
                NotificationRequest.builder()
                        .message("Visita rifiutata")
                        .userEmail(saved.getUser().getSecurityAccountDecorator().getAccountEmail())
                        .build());

        return visitMapper.toResponse(saved);
    }
    */
}
