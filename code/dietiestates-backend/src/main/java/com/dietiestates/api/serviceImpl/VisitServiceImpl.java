package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.api.factory.VisitFactory;
import com.dietiestates.api.finder.RealEstateFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.finder.VisitFinder;
import com.dietiestates.api.mapper.VisitMapper;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.repository.VisitRepository;
import com.dietiestates.api.service.VisitService;
import com.dietiestates.api.verifier.VisitVerifier;
import com.dietiestates.api.service.NotificationService;
import com.dietiestates.api.dto.request.NotificationRequest;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitFactory visitFactory;
    private final VisitFinder visitFinder;
    private final VisitVerifier visitVerifier;
    private final VisitMapper visitMapper;

    private final RealEstateFinder realEstateFinder;
    private final UserFinder userFinder;
    private final NotificationService notificationService;

    @Override
    public VisitResponse createVisit(VisitRequest request, Long realEstateId) {

        var visitSpec = visitMapper.toSpec(request);

        var user = userFinder.getUserByEmail(visitSpec.getUserEmail());
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        var visit = visitFactory.createVisitFromSpec(visitSpec, user, realEstate);
        visitRepository.save(visit);

        return visitMapper.fromEntity(visit);
    }

    @Override
    @Transactional(readOnly = true)
    public VisitResponse getVisitById(
            Long realEstateId,
            Long visitId)
            throws VisitNotOwnedByRealEstateException {
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);
        var visit = visitFinder.getVisitById(visitId);

        visitVerifier.checkVisitOwnedByRealEstate(visit.getRealEstate().getId(), realEstate.getId());

        return visitMapper.fromEntity(visit);
    }

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
}
