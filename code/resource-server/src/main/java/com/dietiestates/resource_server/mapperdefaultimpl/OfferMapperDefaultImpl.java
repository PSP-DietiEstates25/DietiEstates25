package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.enums.RealEstateStatus;
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
                .counterOfId(request.getCounterOfId() != null ? request.getCounterOfId() : null)
				.build();
	}
	
	@Override
	public OfferResponse fromEntity(Offer offer) {

        Long counterOfId = null;
        Long counterOfferId = null;

        if (offer.getCounterOf() != null) {
            counterOfId = offer.getCounterOf().getId();
        }

        if (offer.getCounterOffer() != null) {
            counterOfferId = offer.getCounterOffer().getId();
        }

		return OfferResponse.offerResponseBuilder()
				.id(offer.getId())
				.createdDate(offer.getCreatedDate())
				.lastModifiedDate(offer.getLastModifiedDate())
				.category(offer.getProposalCategory().toString())
				.status(offer.getProposalStatus().toString())
				.userEmail(offer.getNegotiation().getUser().getEmail())
				.realEstateId(offer.getNegotiation().getRealEstate().getId())
                .estateAgentEmail(offer.getNegotiation().getEstateAgent().getEmail())
                .realEstateAddress(offer
                        .getNegotiation()
                        .getRealEstate()
                        .getDetail()
                        .getGeographicalPosition()
                        .getAddress()
                )
				.amount(offer.getAmount())
                .counterOfId(counterOfId)
                .counterOfferId(counterOfferId)
                .isRealEstateDeleted(
                        offer.getNegotiation().getRealEstate().getStatus() != RealEstateStatus.ACTIVE
                )
				.build();
	}

    @Override
    public Page<OfferResponse> createPagedOffersResponse(Page<Offer> offers) {
        return offers.map(this::fromEntity);
    }
}
