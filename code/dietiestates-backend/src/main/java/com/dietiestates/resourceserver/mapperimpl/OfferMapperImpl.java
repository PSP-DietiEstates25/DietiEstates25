package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.OfferRequest;
import com.dietiestates.resourceserver.dto.response.OfferResponse;
import com.dietiestates.resourceserver.mapper.OfferMapper;
import com.dietiestates.resourceserver.model.Offer;
import com.dietiestates.resourceserver.spec.OfferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfferMapperImpl implements OfferMapper {
	
	@Override
	public OfferSpec toSpec(OfferRequest request) {
		return OfferSpec.offerSpecBuilder()
				.category(request.getCategory())
				.status(request.getStatus())
				.userEmail(request.getUserEmail())
				.amount(request.getAmount())
				.build();
	}
	
	@Override
	public OfferResponse fromEntity(Offer offer) {
		return OfferResponse.offerResponseBuilder()
				.id(offer.getId())
				.createdDate(offer.getCreatedDate())
				.lastModifiedDate(offer.getLastModifiedDate())
				.category(offer.getProposalCategory().toString())
				.status(offer.getProposalStatus().toString())
				.userEmail(offer.getUser().getEmail())
				.realEstateId(offer.getRealEstate().getId())
				.amount(offer.getAmount())
				.build();
	}
}
