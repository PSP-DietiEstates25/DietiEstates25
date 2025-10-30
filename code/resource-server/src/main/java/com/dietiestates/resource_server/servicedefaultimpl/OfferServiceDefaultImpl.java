package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.enums.ProposalCategory;
import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.factory.OfferFactory;
import com.dietiestates.resource_server.finder.OfferFinder;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.OfferMapper;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.repository.OfferRepository;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.service.OfferService;
import com.dietiestates.resource_server.spec.OfferSpec;
import com.dietiestates.resource_server.verifier.OfferVerifier;
import com.dietiestates.resource_server.verifier.RealEstateVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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
	private final RealEstateFinder realEstateFinder;
    private final NotificationService notificationService;

	@Override
	public OfferResponse createOffer(OfferRequest request, Long realEstateId, Authentication authentication) {

		var offerSpec = offerMapper.toSpec(request);
        chooseOfferCategory(offerSpec, authentication);

		var user = userFinder.getUserByEmail(offerSpec.getUserEmail());
		var realEstate = realEstateFinder.getRealEstateById(realEstateId);

        Offer counteredOffer = null;
        if(offerSpec.getCategory().equals(ProposalCategory.COUNTER_OFFER.toString())){
            counteredOffer = offerFinder.getOfferById(offerSpec.getCounteredOfferId());
        }

		var offer = offerFactory.createOfferFromSpec(offerSpec, user, realEstate, counteredOffer);
		offerRepository.save(offer);

		return offerMapper.fromEntity(offer);
	}
    /*
	@Override
	public OfferResponse createOffer(OfferRequest request, Long realEstateId) {
		
		var offerSpec = offerMapper.toSpec(request);
		
		var user = userFinder.getUserByEmail(offerSpec.getUserEmail());
		var realEstate = realEstateFinder.getRealEstateById(realEstateId);
		
		var offer = offerFactory.createOfferFromSpec(offerSpec, user, realEstate);
		offerRepository.save(offer);
		
		return offerMapper.fromEntity(offer);
	}
	*/
	
	@Override
	public OfferResponse getOfferById(Long realEstateId, Long offerId) {
		
		var offer = offerFinder.getOfferById(offerId);
		var realEstate = realEstateFinder.getRealEstateById(realEstateId);
		
		return offerMapper.fromEntity(offer);
	}

    @Override
    public Page<OfferResponse> getPagedRealEstateOffers(Long realEstateId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var realEstateOffers = offerRepository.findByRealEstateId(realEstateId, pageable);

        return offerMapper.createPagedOffersResponse(realEstateOffers);
    }

    @Override
    public OfferResponse updateOfferStatus(OfferRequest request, Long realEstateId, Long offerId) {

        offerVerifier.checkOfferExists(offerId);
        offerVerifier.checkOfferOwnedByRealEstate(offerId, realEstateId);

        var offerSpec = offerMapper.toSpec(request);
        var offerToUpdate = offerFinder.getOfferByRealEstate(offerId, realEstateId);
        offerToUpdate.setProposalStatus(ProposalStatus.valueOf(offerSpec.getStatus()));

        createOfferNotification(offerToUpdate);

        offerRepository.save(offerToUpdate);
        return offerMapper.fromEntity(offerToUpdate);
    }

    public void createOfferNotification(Offer offer){

        String message = null;

        if (offer.getProposalCategory().equals(ProposalCategory.OFFER)){
            if (offer.getProposalStatus().equals(ProposalStatus.ACCEPTED))
                message = "Offer accepted";
            else if (offer.getProposalStatus().equals(ProposalStatus.REJECTED))
                message = "Offer rejected";
        } else if (offer.getProposalCategory().equals(ProposalCategory.COUNTER_OFFER)){
            message = "Offer countered";
        }

        notificationService.createNotification(
                NotificationCategoryType.OFFER.toString(),
                NotificationRequest.builder()
                        .message(message)
                        .userEmail(offer.getUser().getEmail())
                        .build()
        );
    }

    @Override
    public void chooseOfferCategory(OfferSpec offerSpec, Authentication authentication){
        if(authentication.getAuthorities().contains("SCOPE_ESTATE_AGENT")) {
            offerSpec.setCategory(ProposalCategory.COUNTER_OFFER.toString());
        }
        else if(authentication.getAuthorities().contains("SCOPE_USER"))
            offerSpec.setCategory(ProposalCategory.OFFER.toString());
    }

}
