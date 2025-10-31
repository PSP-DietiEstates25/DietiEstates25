package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.SearchRealEstate;

import java.util.List;

public interface SearchRealEstateService {

	void createSearchRealEstate(Search search, List<RealEstate> realEstates);

    void createRealEstateSearch(RealEstate realEstate, List<Search> searches);

    List<RealEstate> getSearchedRealEstates(Search search);

    List<Search> createRealEstateSearchesLink(RealEstate realEstate);

    List<Search> getSearchesByRealEstateFilter(RealEstate realEstate);

    List<RealEstate> getRealEstatesBySearchFilter(Search search);
}
