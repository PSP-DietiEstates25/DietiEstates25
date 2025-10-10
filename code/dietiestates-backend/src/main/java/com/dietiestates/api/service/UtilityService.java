package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;

public interface UtilityService {

	UtilityResponse createUtility(UtilityRequest request);
	
	UtilityResponse getUtilityById(Long utilityId);
}
