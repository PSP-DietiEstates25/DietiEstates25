package com.dietiestates.api.factory;

import java.util.List;

import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.SearchRealEstate;

public interface SearchRealEstateFactory {

	SearchRealEstate createSearchRealEstate(Search search, List<RealEstate> realEstates);
}
