package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;

import java.util.List;

public interface SearchRealEstateMatchingService {
    List<Search> getSearchesByRealEstateFilter(RealEstate realEstate);
    List<RealEstate> getRealEstatesBySearchFilter(Search search);
}
