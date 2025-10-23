package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.dto.response.EstateAgentResponse;
import com.dietiestates.resourceserver.model.EstateAgent;
import com.dietiestates.resourceserver.spec.StafferSpec;

public interface EstateAgentMapper {

	StafferSpec toSpec(StafferRequest request);
	
	EstateAgentResponse fromEntity(EstateAgent estateAgent);
}
