package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;
import com.dietiestates.api.mapper.OfferMapper;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.spec.OfferSpec;

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
				.userEmail(offer.getUser().getSecurityAccountDecorator().getAccountEmail())
				.realEstateId(offer.getRealEstate().getId())
				.amount(offer.getAmount())
				.build();
	}

	@Override
	public OfferResponse toResponse(Offer offer) {
		return fromEntity(offer);
	}
}
