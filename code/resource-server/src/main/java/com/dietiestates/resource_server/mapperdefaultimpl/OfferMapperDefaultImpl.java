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
		return OfferSpec.offerSpecBuilder()
				.category(request.getCategory())
				.status(request.getStatus())
				.amount(request.getAmount())
                .counterOfId(request.getCounterOfId())
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
				.userEmail(offer.getNegotiation().getUser().getEmail())
				.realEstateId(offer.getNegotiation().getRealEstate().getId())
                .estateAgentEmail(offer.getNegotiation().getEstateAgent().getEmail())
				.amount(offer.getAmount())
                .counterOfId(offer.getCounterOf().getId())
                .counterOfferId(offer.getCounterOffer().getId())
				.build();
	}

    @Override
    public Page<OfferResponse> createPagedOffersResponse(Page<Offer> offers) {
        return offers.map(this::fromEntity);
    }
}
