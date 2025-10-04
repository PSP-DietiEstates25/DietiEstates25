package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.model.Visit;

public interface VisitFactory {

	Visit createVisit(VisitRequest request, Long realEstateId);
}
