package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;

import java.util.List;

public interface SearchService {
	List<RealEstateResponse> createSearch(SearchRequest request, String userEmail);
}
