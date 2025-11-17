package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RealEstateService {
	RealEstateResponse createRealEstate(RealEstateRequest request, String estateAgentEmail);
	RealEstateResponse getRealEstateById(Long id);
    RealEstateResponse updateRealEstate(Long realEstateId, RealEstateRequest request, String estateAgentEmail);
    Page<RealEstateResponse> getRealEstates(Integer page, Integer size);
    Page<RealEstateResponse> getEstateAgentRealEstates(String estateAgentEmail, Integer page, Integer size);
    Page<RealEstateResponse> getAdminRealEstates(String adminEmail, Integer page, Integer size);
    Page<RealEstateResponse> getSearchRealEstates(Long searchId, Integer page, Integer size);
    void deleteRealEstate(Long realEstateId);
}
