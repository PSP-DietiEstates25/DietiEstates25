package com.dietiestates.resourceserver.factory;

import java.util.List;

import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.SearchRealEstate;

public interface SearchRealEstateFactory {

	SearchRealEstate createSearchRealEstate(Search search, List<RealEstate> realEstates);
}
