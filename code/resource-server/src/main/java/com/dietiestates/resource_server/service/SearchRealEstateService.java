package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.SearchRealEstate;

import java.util.List;

public interface SearchRealEstateService {

	void createSearchRealEstate(Search search, List<RealEstate> realEstates);
	
	SearchRealEstate of(Search search, RealEstate realEstate);
}
