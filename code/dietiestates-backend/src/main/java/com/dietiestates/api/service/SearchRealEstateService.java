package com.dietiestates.api.service;

import java.util.List;

import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.SearchRealEstate;

public interface SearchRealEstateService {

	void createSearchRealEstate(Search search, List<RealEstate> realEstates);
	
	SearchRealEstate of(Search search, RealEstate realEstate);
}
