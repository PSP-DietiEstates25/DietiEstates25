package com.dietiestates.resource_server.factory;

import java.util.List;

import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.SearchRealEstate;

public interface SearchRealEstateFactory {

    SearchRealEstate createSearchRealEstate(Search search, RealEstate realEstate);
}
