package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.enums.ProposalCategory;
import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.factory.OfferFactory;
import com.dietiestates.resource_server.finder.*;
import com.dietiestates.resource_server.mapper.OfferMapper;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.repository.OfferRepository;
import com.dietiestates.resource_server.service.NegotiationService;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.service.OfferService;
import com.dietiestates.resource_server.spec.NegotiationSpec;
import com.dietiestates.resource_server.verifier.OfferVerifier;
import com.dietiestates.resource_server.verifier.RealEstateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferServiceDefaultImpl implements OfferService {

	private final OfferRepository offerRepository;
	private final OfferFactory offerFactory;
	private final OfferFinder offerFinder;
	private final OfferMapper offerMapper;
    private final OfferVerifier offerVerifier;
	
	private final UserFinder userFinder;
    private final EstateAgentFinder estateAgentFinder;
	private final RealEstateFinder realEstateFinder;
    private final RealEstateVerifier realEstateVerifier;

    private final NotificationService notificationService;
    private final NegotiationService negotiationService;

    @Override
    public OfferResponse createOffer(OfferRequest request, Long realEstateId, String creatorEmail, String creatorRole) {
        if(creatorRole.equals("USER"))
            return this.createUserOffer(request, realEstateId, creatorEmail);
        else return this.createEstateAgentCounterOffer(request, realEstateId, creatorEmail);
    }

	@Override
    public OfferResponse createUserOffer(OfferRequest request, Long realEstateId, String userEmail) {

		var offerSpec = offerMapper.toSpec(request);
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        var negotiationSpec = NegotiationSpec.builder()
                .realEstateId(realEstateId)
                .userEmail(userEmail)
                .estateAgentEmail(realEstate.getEstateAgent().getEmail())
                .build();
        var negotiation = negotiationService.setupNegotiation(negotiationSpec);

		var offer = offerFactory.createOfferFromSpec(offerSpec, negotiation);
        offer.setProposalCategory(ProposalCategory.OFFER);
		offerRepository.save(offer);

		return offerMapper.fromEntity(offer);
	}

    @Override
    public OfferResponse createEstateAgentCounterOffer(OfferRequest request, Long realEstateId, String estateAgentEmail){

        var offerSpec = offerMapper.toSpec(request);
        var userOffer = offerFinder.getOfferById(offerSpec.getCounterOfId());
        var negotiation = userOffer.getNegotiation();

        var counterOffer = offerFactory.createOfferFromSpec(offerSpec, negotiation);
        counterOffer.setProposalCategory(ProposalCategory.COUNTER_OFFER);
        counterOffer.setCounterOfOffer(userOffer);
        offerRepository.save(counterOffer);

        return offerMapper.fromEntity(counterOffer);
    }
	
	@Override
	public OfferResponse getOfferById(Long realEstateId, Long offerId) {
        offerVerifier.checkOfferOwnedByRealEstate(offerId, realEstateId);
		var offer = offerFinder.getOfferById(offerId);

		return offerMapper.fromEntity(offer);
	}

    public Page<OfferResponse> getRealEstateOffers(Long realEstateId, String creatorEmail, String creatorRole, Integer page, Integer size) {

        if(creatorRole.equals("USER"))
            return this.getRealEstateUserOffers(realEstateId, creatorEmail, page, size);
        else return this.getRealEstateEstateAgentOffers(realEstateId, creatorEmail, page, size);
    }

    @Override
    public Page<OfferResponse> getRealEstateUserOffers(Long realEstateId, String userEmail, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var user = userFinder.getUserByEmail(userEmail);
        var realEstateUserOffers = offerFinder.getRealEstateUserOffers(realEstateId, user.getId(), pageable);

        return offerMapper.createPagedOffersResponse(realEstateUserOffers);
    }

    @Override
    public Page<OfferResponse> getRealEstateEstateAgentOffers(Long realEstateId, String estateAgentEmail, Integer page, Integer size){

        realEstateVerifier.checkRealEstateOwnedByEstateAgent(realEstateId, estateAgentEmail);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
        var realEstateEstateAgentOffers = offerFinder.getRealEstateEstateAgentOffers(realEstateId, estateAgent.getId(), pageable);

        return offerMapper.createPagedOffersResponse(realEstateEstateAgentOffers);
    }

    @Override
    public Page<OfferResponse> getOffers(String creatorEmail, String creatorRole, String status, Integer page, Integer size) {
        if(creatorRole.equals("USER"))
            return this.getAllUserOffers(creatorEmail, status, page, size);
        else return this.getAllEstateAgentOffers(creatorEmail, status, page, size);
    }

    @Override
    public Page<OfferResponse> getAllUserOffers(String userEmail, String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var user = userFinder.getUserByEmail(userEmail);
        var offers = offerFinder.getAllUserOffers(user.getId(), status, pageable);
        return offerMapper.createPagedOffersResponse(offers);
    }

    @Override
    public Page<OfferResponse> getAllEstateAgentOffers(String estateAgentEmail, String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
        var offers = offerFinder.getAllEstateAgentOffers(estateAgent.getId(), status, pageable);
        return offerMapper.createPagedOffersResponse(offers);
    }

    @Override
    public OfferResponse updateOfferStatus(OfferRequest request, Long realEstateId, Long offerId) {

        offerVerifier.checkOfferOwnedByRealEstate(offerId, realEstateId);

        var offerSpec = offerMapper.toSpec(request);
        var offerToUpdate = offerFinder.getOfferById(offerId);
        offerToUpdate.setProposalStatus(ProposalStatus.valueOf(offerSpec.getStatus()));

        createOfferNotification(offerToUpdate);

        offerRepository.save(offerToUpdate);
        return offerMapper.fromEntity(offerToUpdate);
    }

    public void createOfferNotification(Offer offer){

        String message = null;
        String estateAgentEmail = offer.getNegotiation().getEstateAgent().getEmail();
        String realEstateAddress = offer.getNegotiation().getRealEstate().getDetail().getGeographicalPosition().getAddress();
        String offerAmount = offer.getAmount().toString() + "€";

        String counterOfferAmount = null;

        if(offer.getCounterOffer() != null)
            counterOfferAmount = offer.getCounterOffer().getAmount().toString() + "€";

        if (offer.getProposalCategory().equals(ProposalCategory.OFFER)){
            if (offer.getProposalStatus().equals(ProposalStatus.ACCEPTED))
                message = "L'offerta di " + offerAmount + " per l'immobile in " + realEstateAddress + " è stata accettata, contatta l'agente al seguente recapito: " + estateAgentEmail + ".";
            else if (offer.getProposalStatus().equals(ProposalStatus.REJECTED))
                message = "L'offerta di " + offerAmount + " per l'immobile in " + realEstateAddress + " è stata rifiutata, riprova con una nuova offerta.";
            else if (offer.getProposalStatus().equals(ProposalStatus.COUNTERED))
                message = "L'offerta di " + offerAmount + " per l'immobile in" + realEstateAddress + " è stata negoziata, con una controfferta dal valore di " + counterOfferAmount + ".";
        } else if (offer.getProposalCategory().equals(ProposalCategory.COUNTER_OFFER)){
            message = "Offer countered2";
        }

        notificationService.createNotification(
                NotificationCategoryType.OFFER.toString(),
                NotificationRequest.builder()
                        .message(message)
                        .build()
        );
    }

}
