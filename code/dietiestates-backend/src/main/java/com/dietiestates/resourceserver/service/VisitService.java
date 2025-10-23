package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.VisitRequest;
import com.dietiestates.resourceserver.dto.response.VisitResponse;
import com.dietiestates.resourceserver.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitService {

	VisitResponse createVisit(VisitRequest request, Long realEstateId);
	
	VisitResponse getVisitById(
			Long realEstateId,
			Long visitId
			)
		throws VisitNotOwnedByRealEstateException;
}
