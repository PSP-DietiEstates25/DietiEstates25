package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.DetailRequest;
import com.dietiestates.resource_server.dto.response.DetailResponse;

public interface DetailService {
	DetailResponse createDetail(DetailRequest request);
	DetailResponse getDetailById(Long detailId);
    DetailResponse getRealEstateDetail(Long realEstateId);
    DetailResponse getSearchDetail(Long searchId);
    void updateDetail(Long detailId, DetailRequest requeust);
}
