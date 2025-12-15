package com.dietiestates.resource_server.filter;

import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Utility;

import java.util.List;

public interface RealEstateFilter {
    List<RealEstate> filterRealEstateByGeographicalPosition(GeographicalPosition geographicalPosition, List<RealEstate> realEstatesToFilter);
    List<RealEstate> filterRealEstateByUtility(Utility utility, List<RealEstate> realEstatesToFilter);

    boolean matchesUtilities(Utility searchUtility, Utility realEstateUtility);

    List<RealEstate> filterRealEstateByCadastralFilters(CadastralFilter cadastralFilter, List<RealEstate> realEstatesToFilter);
}
