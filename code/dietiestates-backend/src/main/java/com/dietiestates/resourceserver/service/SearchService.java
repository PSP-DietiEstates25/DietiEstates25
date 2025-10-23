package com.dietiestates.resourceserver.service;

import java.util.List;

import com.dietiestates.resourceserver.dto.request.SearchRequest;
import com.dietiestates.resourceserver.dto.response.RealEstateResponse;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;

public interface SearchService {

	List<RealEstateResponse> createSearch(SearchRequest request);
	
	void createSearchRealEstate(Search search, List<RealEstate> searchRealEstates);
	
	List<RealEstate> getSearchedRealEstates(Search search);
}
