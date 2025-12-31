package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RealEstateService {
	RealEstateResponse createRealEstate(RealEstateRequest request, List<MultipartFile> images, String estateAgentEmail) throws IOException;
	RealEstateResponse getRealEstateById(Long id);
    RealEstateResponse updateRealEstate(Long realEstateId, RealEstateRequest request, List<MultipartFile> images, String estateAgentEmail) throws IOException;
    Page<RealEstateResponse> getRealEstates(Integer page, Integer size);
    Page<RealEstateResponse> getEstateAgentRealEstates(String estateAgentEmail, Integer page, Integer size);
    Page<RealEstateResponse> getAdminRealEstates(String adminEmail, Integer page, Integer size);
    Page<RealEstateResponse> getSearchRealEstates(Long searchId, Integer page, Integer size);
    void deleteRealEstate(Long realEstateId, String stafferEmail, String stafferRole);
}
