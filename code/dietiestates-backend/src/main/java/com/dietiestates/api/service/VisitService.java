package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.VisitRequest;

public interface VisitService {

	void createVisit(VisitRequest request, Long realEstateId);
}
