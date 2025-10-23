package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.UtilityRequest;
import com.dietiestates.resourceserver.dto.response.UtilityResponse;

public interface UtilityService {

	UtilityResponse createUtility(UtilityRequest request);
	
	UtilityResponse getUtilityById(Long utilityId);
}
