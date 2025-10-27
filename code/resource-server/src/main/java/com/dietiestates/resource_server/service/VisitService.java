package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitService {

	VisitResponse createVisit(VisitRequest request, Long realEstateId);
	
	VisitResponse getVisitById(
			Long realEstateId,
			Long visitId
			)
		throws VisitNotOwnedByRealEstateException;
}
