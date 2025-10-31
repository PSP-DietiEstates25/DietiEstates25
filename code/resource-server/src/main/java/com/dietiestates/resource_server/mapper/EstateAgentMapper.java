package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.EstateAgentResponse;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.spec.StafferSpec;

public interface EstateAgentMapper {

	StafferSpec toSpec(StafferRequest request);
	
	EstateAgentResponse fromEntity(EstateAgent estateAgent);
}
