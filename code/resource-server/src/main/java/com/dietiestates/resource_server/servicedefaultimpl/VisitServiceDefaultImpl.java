package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.enums.ProposalCategory;
import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resource_server.factory.VisitFactory;
import com.dietiestates.resource_server.finder.*;
import com.dietiestates.resource_server.mapper.VisitMapper;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.repository.VisitRepository;
import com.dietiestates.resource_server.service.NegotiationService;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.service.VisitService;
import com.dietiestates.resource_server.spec.NegotiationSpec;
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

    private static final String createdDate =  "createdDate";

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

        Pageable pageable = PageRequest.of(page, size, Sort.by(createdDate).descending());
        var realEstateVisits = visitFinder.getRealEstateVisits(realEstateId, pageable);

        return visitMapper.createPagedVisitsResponse(realEstateVisits);
    }

    @Override
    public Page<VisitResponse> getAllEstateAgentVisits(String estateAgentEmail, String status, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(createdDate).descending());
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
        visitToUpdate.setProposalStatus(ProposalStatus.valueOf(visitSpec.getStatus()));

        createVisitNotification(visitToUpdate);

        visitRepository.save(visitToUpdate);
        return visitMapper.fromEntity(visitToUpdate);
    }

    public void createVisitNotification(Visit visit){

        var message = createVisitNotificationMessage(visit);

        var notificationRequest = NotificationRequest.builder()
                .message(message)
                .notificationCategory(NotificationCategory.VISIT.toString())
                .isVisible(true)
                .negotiationId(visit.getNegotiation().getId())
                .build();

        notificationService.createNotification(notificationRequest);
    }

    public String createVisitNotificationMessage(Visit visit){
        String message = null;
        String visitMessage = "La visita prenotata in data ";
        String realEstateMessage = " per l'immobile in ";
        String quotationMark = "\"";
        String alle = " alle ";
        String estateAgentEmail = visit.getNegotiation().getEstateAgent().getEmail();
        String realEstateAddress = visit.getNegotiation().getRealEstate().getDetail().getGeographicalPosition().getAddress();

        if (visit.getProposalCategory().equals(ProposalCategory.VISIT)){
            if (visit.getProposalStatus().equals(ProposalStatus.ACCEPTED))
                message = visitMessage + visit.getDate().toString() + alle + visit.getTime().toString() + realEstateMessage + quotationMark + realEstateAddress + quotationMark + " è stata accettata, contatta l'agente al seguente recapito: " + estateAgentEmail + ".";
            else if (visit.getProposalStatus().equals(ProposalStatus.REJECTED))
                message = visitMessage + visit.getDate().toString() + alle + visit.getTime().toString() + realEstateMessage + quotationMark + realEstateAddress + quotationMark + " è stata rifiutata, riprova con una nuova prenotazione.";
        }

        return message;
    }
}
