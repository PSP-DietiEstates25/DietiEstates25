package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.OfferDto;
import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.repository.OfferRepository;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferService {

	private final OfferRepository offerRepository;
	private final UserRepository userRepository;
	private final RealEstateRepository realEstateRepository;
	
	public void createOffer(OfferDto request, Long realEstateId) {
		var offer = of(request, realEstateId);
		offerRepository.save(offer);
	}
	
	public Offer of(OfferDto request, Long realEstateId) {
		var user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
		
		var realEstate = realEstateRepository.findById(realEstateId)
				.orElseThrow(RealEstateNotFoundException::new);
		
		return Offer.offerBuilder()
				.createdDate(LocalDateTime.now())
				.category(ProposalCategory.valueOf(request.getCategory()))
				.status(ProposalStatus.valueOf(request.getStatus()))
				.user(user)
				.realEstate(realEstate)
				.amount(request.getAmount())
				.build();
	}
	/*
    private final OfferRepository offerRepo;
    private final RealEstateAdRepository realEstateRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    @Transactional
    public Offer propose(String requesterEmail, Long adId, OfferProposalRequest req) {
        User requester = userRepo.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requesterEmail));

        // Consenti solo CLIENT
        boolean isAgent = requester.getRoles().stream().anyMatch(r -> "AGENT".equalsIgnoreCase(r.getName()));
        boolean isAdmin = requester.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getName()));
        if (isAgent || isAdmin) {
            throw new AccessDeniedException("Solo i CLIENT possono proporre offerte.");
        }

        RealEstate ad = realEstateRepo.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Real estate not found: " + adId));

        User agent = ad.getEstateAgent();
        if (agent == null) {
            throw new IllegalStateException("Annuncio senza agente associato.");
        }

        if (req.getAmount() == null || req.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        Offer offer = Offer.builder()
                .realEstate(ad)
                .user(requester)
                .estateAgent(agent)
                .amount(req.getAmount())
                .build();

        Offer saved = offerRepo.save(offer);

        // notifica all’agente
        notificationService.push(
                agent.getEmail(),
                NotificationCategoryType.OFFER,
                "Nuova offerta ricevuta",
                "Hai ricevuto una nuova offerta di " + req.getAmount() + " € per \"" + ad.getAddress() + "\".",
                ad.getId());

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Offer> listForEstate(String requesterEmail, Long adId, Integer page, Integer size) {
        User requester = userRepo.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requesterEmail));

        RealEstate ad = realEstateRepo.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Real estate not found: " + adId));

        boolean isAdmin = requester.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getName()));
        boolean isAgent = requester.getRoles().stream().anyMatch(r -> "AGENT".equalsIgnoreCase(r.getName()));

        // se è AGENT deve essere il proprietario dell'annuncio
        if (isAgent && !isAdmin) {
            if (ad.getEstateAgent() == null || !ad.getEstateAgent().getId().equals(requester.getId())) {
                throw new AccessDeniedException(
                        "Non puoi visualizzare le offerte di un annuncio che non ti appartiene.");
            }
        }

        // ADMIN vede tutto
        Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
        return offerRepo
                .findByEstateAgent_EmailAndRealEstate_IdOrderByCreatedDateDesc(
                        ad.getEstateAgent().getEmail(), adId, pageable)
                .getContent();
    }

    private int safePage(Integer p) {
        return (p != null && p >= 0) ? p : 0;
    }

    private int safeSize(Integer s) {
        return (s != null && s > 0) ? s : 12;
    }
    */
}
