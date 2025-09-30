package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;
import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;

@Component
public class OfferMapper {

	public Offer toEntity(OfferRequest request, User user, RealEstate realEstate) {
		return Offer.offerBuilder()
				.createdDate(LocalDateTime.now())
				.category(ProposalCategory.valueOf(request.getCategory()))
				.status(ProposalStatus.valueOf(request.getStatus()))
				.user(user)
				.realEstate(realEstate)
				.amount(request.getAmount())
				.build();
	}
	
	public OfferResponse fromEntity(Offer offer) {
		return OfferResponse.offerResponseBuilder()
				.id(offer.getId())
				.createdDate(offer.getCreatedDate())
				.lastModifiedDate(offer.getLastModifiedDate())
				.category(offer.getCategory().toString())
				.status(offer.getStatus().toString())
				.userEmail(offer.getUser().getEmail())
				.realEstateId(offer.getRealEstate().getId())
				.amount(offer.getAmount())
				.build();
	}
}
