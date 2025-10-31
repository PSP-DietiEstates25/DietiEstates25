package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.mapper.OfferMapper;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.spec.OfferSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferMapperDefaultImpl implements OfferMapper {
	
	@Override
	public OfferSpec toSpec(OfferRequest request) {

		var offerSpec = OfferSpec.offerSpecBuilder()
				.category(request.getCategory())
				.status(request.getStatus())
				.userEmail(request.getUserEmail())
				.amount(request.getAmount())
                .counteredOfferId(
                        request.getCounteredOfferId() != null ? request.getCounteredOfferId() : null
                )
				.build();

        offerSpec.setCounteredOfferId(request.getCounteredOfferId());
        return offerSpec;
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
                .counteredOfferId(
                        offer.getCounteredOffer() != null ? offer.getCounteredOffer().getId() : null
                )
				.build();
	}

    @Override
    public Page<OfferResponse> createPagedOffersResponse(Page<Offer> offers) {
        return offers.map(this::fromEntity);
    }
}
