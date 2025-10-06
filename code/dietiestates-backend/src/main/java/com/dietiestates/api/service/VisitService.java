package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitService {

	void createVisit(VisitRequest request, Long realEstateId);
	
	VisitResponse getVisitById(
			Long realEstateId,
			Long visitId
			)
		throws VisitNotOwnedByRealEstateException;
}
