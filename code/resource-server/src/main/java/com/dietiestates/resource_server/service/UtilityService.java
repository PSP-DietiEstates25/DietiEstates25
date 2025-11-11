package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.UtilityRequest;
import com.dietiestates.resource_server.dto.response.UtilityResponse;

public interface UtilityService {
	UtilityResponse createUtility(UtilityRequest request);
	UtilityResponse getUtilityById(Long utilityId);
    UtilityResponse getDetailUtility(Long detailId);
    UtilityResponse updateUtility(Long utilityId, UtilityRequest request);
}
