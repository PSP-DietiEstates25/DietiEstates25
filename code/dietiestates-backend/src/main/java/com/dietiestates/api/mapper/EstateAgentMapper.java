package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.dto.response.EstateAgentResponse;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.spec.StafferSpec;

public interface EstateAgentMapper {

	StafferSpec toSpec(StafferRequest request);
	
	EstateAgentResponse fromEntity(EstateAgent estateAgent);
}
