package com.dietiestates.resourceserver.service;

import java.util.List;

import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.SearchRealEstate;

public interface SearchRealEstateService {

	void createSearchRealEstate(Search search, List<RealEstate> realEstates);
	
	SearchRealEstate of(Search search, RealEstate realEstate);
}
