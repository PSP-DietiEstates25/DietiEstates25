package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.spec.OfferSpec;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OfferMapper {

	OfferSpec toSpec(OfferRequest request);
	
	OfferResponse fromEntity(Offer offer);

    Page<OfferResponse> createPagedOffersResponse(Page<Offer> offers);
}
