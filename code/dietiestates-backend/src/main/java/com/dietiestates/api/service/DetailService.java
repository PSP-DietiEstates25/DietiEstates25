package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;

public interface DetailService {

	DetailResponse createDetail(DetailRequest request);
	
	DetailResponse getDetailById(Long detailId);
}
