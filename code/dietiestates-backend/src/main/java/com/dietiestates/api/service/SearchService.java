package com.dietiestates.api.service;

import java.util.List;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;

public interface SearchService {

	List<RealEstateResponse> createSearch(SearchRequest request);
	
	void createSearchRealEstate(Search search, List<RealEstate> searchRealEstates);
	
	List<RealEstate> getSearchedRealEstates(Search search);
}
