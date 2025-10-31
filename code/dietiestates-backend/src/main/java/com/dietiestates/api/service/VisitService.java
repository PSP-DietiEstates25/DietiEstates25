package com.dietiestates.api.service;

import org.springframework.security.core.Authentication;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.exception.notowned.VisitNotOwnedByRealEstateException;

public interface VisitService {

	VisitResponse createVisit(VisitRequest request, Long realEstateId);

	VisitResponse getVisitById(
			Long realEstateId,
			Long visitId)
			throws VisitNotOwnedByRealEstateException;

	VisitResponse acceptVisit(Long id, Authentication auth);

	VisitResponse rejectVisit(Long id, Authentication auth);
}
