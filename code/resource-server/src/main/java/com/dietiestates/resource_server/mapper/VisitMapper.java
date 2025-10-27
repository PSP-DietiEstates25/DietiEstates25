package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.spec.VisitSpec;

public interface VisitMapper {

	VisitSpec toSpec(VisitRequest request);
	
	VisitResponse fromEntity(Visit visit);
}
