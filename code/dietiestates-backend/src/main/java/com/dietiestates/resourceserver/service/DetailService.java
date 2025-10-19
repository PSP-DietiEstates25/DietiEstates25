package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.DetailRequest;
import com.dietiestates.resourceserver.dto.response.DetailResponse;

public interface DetailService {

	DetailResponse createDetail(DetailRequest request);
	
	DetailResponse getDetailById(Long detailId);
}
