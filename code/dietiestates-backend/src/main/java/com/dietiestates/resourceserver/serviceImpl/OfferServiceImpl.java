package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.OfferRequest;
import com.dietiestates.resourceserver.dto.response.OfferResponse;
import com.dietiestates.resourceserver.factory.OfferFactory;
import com.dietiestates.resourceserver.finder.OfferFinder;
import com.dietiestates.resourceserver.finder.RealEstateFinder;
import com.dietiestates.resourceserver.finder.UserFinder;
import com.dietiestates.resourceserver.mapper.OfferMapper;
import com.dietiestates.resourceserver.repository.OfferRepository;
import com.dietiestates.resourceserver.service.OfferService;

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
	public OfferResponse getOfferById(Long realEstateId, Long offerId) {
		
		var offer = offerFinder.getOfferById(offerId);
		var realEstate = realEstateFinder.getRealEstateById(realEstateId);
		
		return offerMapper.fromEntity(offer);
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
