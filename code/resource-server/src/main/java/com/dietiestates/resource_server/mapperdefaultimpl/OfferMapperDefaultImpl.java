package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.mapper.OfferMapper;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.spec.OfferSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferMapperDefaultImpl implements OfferMapper {
	
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
