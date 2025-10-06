package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;

public interface UtilityService {

	void createUtility(UtilityRequest request, Long detailId);
	
	UtilityResponse getUtilityById(Long detailId, Long utilityId);
}
