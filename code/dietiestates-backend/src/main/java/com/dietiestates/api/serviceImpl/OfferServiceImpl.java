package com.dietiestates.api.serviceImpl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.request.CounterOfferRequest;
import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.factory.OfferFactory;
import com.dietiestates.api.finder.OfferFinder;
import com.dietiestates.api.finder.RealEstateFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.mapper.OfferMapper;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.repository.OfferRepository;
import com.dietiestates.api.service.OfferService;
import com.dietiestates.api.service.NotificationService;
import com.dietiestates.api.dto.request.NotificationRequest;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferFactory offerFactory;
    private final OfferFinder offerFinder;
    private final OfferMapper offerMapper;

    private final UserFinder userFinder;
    private final RealEstateFinder realEstateFinder;
    private final NotificationService notificationService;

    @Override
    public OfferResponse createOffer(OfferRequest request, Long realEstateId) {

        var offerSpec = offerMapper.toSpec(request);

        var user = userFinder.getUserByEmail(offerSpec.getUserEmail());
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        var offer = offerFactory.createOfferFromSpec(offerSpec, user, realEstate);
        offerRepository.save(offer);

        return offerMapper.fromEntity(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferResponse getOfferById(Long realEstateId, Long offerId) {

        var offer = offerFinder.getOfferById(offerId);
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        return offerMapper.fromEntity(offer);
    }

    @Transactional
    public OfferResponse acceptOffer(Long id, Authentication auth) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + id));

        offer.setProposalStatus(ProposalStatus.ACCEPTED);
        var saved = offerRepository.save(offer);

        notificationService.createNotification(
                "OFFER",
                NotificationRequest.builder()
                        .message("Offerta accettata")
                        .userEmail(saved.getUser().getSecurityAccountDecorator().getAccountEmail())
                        .build());

        return offerMapper.toResponse(saved);
    }

    @Transactional
    public OfferResponse rejectOffer(Long id, Authentication auth) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + id));

        offer.setProposalStatus(ProposalStatus.REJECTED);
        var saved = offerRepository.save(offer);

        notificationService.createNotification(
                "OFFER",
                NotificationRequest.builder()
                        .message("Offerta rifiutata")
                        .userEmail(saved.getUser().getSecurityAccountDecorator().getAccountEmail())
                        .build());

        return offerMapper.toResponse(saved);
    }

    @Transactional
    public OfferResponse counterOffer(Long id, CounterOfferRequest req, Authentication auth) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + id));

        offer.setAmount(req.amount());
        offer.setProposalStatus(ProposalStatus.COUNTERED);
        var saved = offerRepository.save(offer);

        notificationService.createNotification(
                "OFFER",
                NotificationRequest.builder()
                        .message("Controfferta inviata")
                        .userEmail(saved.getUser().getSecurityAccountDecorator().getAccountEmail())
                        .build());

        return offerMapper.toResponse(saved);
    }

}
