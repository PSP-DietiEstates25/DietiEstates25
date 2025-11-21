package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.spec.RealEstateSpec;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RealEstateMapper {
	RealEstateSpec toSpec(RealEstateRequest request, List<String> images);
	RealEstateResponse fromEntity(RealEstate realEstate);
    Page<RealEstateResponse> createPagedRealEstatesResponse(Page<RealEstate> realEstates);
    List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates);
}
