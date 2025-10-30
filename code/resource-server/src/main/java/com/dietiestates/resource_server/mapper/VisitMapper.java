package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.spec.VisitSpec;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VisitMapper {

	VisitSpec toSpec(VisitRequest request);
	
	VisitResponse fromEntity(Visit visit);

    List<VisitResponse> createVisitsResponse(List<Visit> visits);

    Page<VisitResponse> createPagedVisitsResponse(Page<Visit> visits);
}
