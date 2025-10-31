package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.spec.VisitSpec;

public interface VisitMapper {

	VisitSpec toSpec(VisitRequest request);

	VisitResponse fromEntity(Visit visit);

	VisitResponse toResponse(Visit visit);
}
