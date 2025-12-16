package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.dto.response.SearchResponse;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {
	List<RealEstateResponse> createSearch(SearchRequest request, String userEmail);
    Page<SearchResponse> getUserSearches(String userEmail, Integer page, Integer size);
    List<RealEstateResponse> runSavedSearch(Long searchId, String userEmail);
}
