package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import org.springframework.data.domain.Page;

public interface VisitService {
	VisitResponse createVisit(VisitRequest request, Long realEstateId, String userEmail);
	VisitResponse getVisitById(Long realEstateId, Long visitId) throws VisitNotOwnedByRealEstateException;
    Page<VisitResponse> getPagedUserRealEstateVisits(Long realEstateId, String userEmail, Integer page, Integer size);
    Page<VisitResponse> getPagedEstateAgentRealEstateVisits(Long realEstateId, String estateAgentEmail, Integer page, Integer size);
    VisitResponse updateVisitStatus(VisitRequest request, Long realEstateId, Long visitId) throws VisitNotOwnedByRealEstateException;
}
