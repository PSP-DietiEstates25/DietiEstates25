package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RealEstateService {

	RealEstateResponse createRealEstate(RealEstateRequest request);
	
	RealEstateResponse getRealEstateById(Long id);

    Page<RealEstateResponse> getPagedRealEstates(Integer page, Integer size);

    RealEstateResponse updateRealEstate(Long realEstateId, RealEstateRequest request);

    void deleteRealEstate(Long realEstateId);
	
}
