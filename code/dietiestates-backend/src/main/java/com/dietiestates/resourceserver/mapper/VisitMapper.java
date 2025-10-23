package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.VisitRequest;
import com.dietiestates.resourceserver.dto.response.VisitResponse;
import com.dietiestates.resourceserver.model.Visit;
import com.dietiestates.resourceserver.spec.VisitSpec;

public interface VisitMapper {

	VisitSpec toSpec(VisitRequest request);
	
	VisitResponse fromEntity(Visit visit);
}
