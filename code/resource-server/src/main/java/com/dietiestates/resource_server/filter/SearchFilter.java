package com.dietiestates.resource_server.filter;

import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.Utility;

import java.util.List;

public interface SearchFilter {
    List<Search>  filterSearchesByGeographicalPosition(GeographicalPosition geographicalPosition, List<Search> searchesToFilter);
    List<Search> filterSearchesByUtility(Utility utility, List<Search> searchesToFilter);

    boolean matchesUtilities(Utility searchUtility, Utility realEstateUtility);

    List<Search> filtlerSearchesByCadastralData(CadastralData cadastralData, List<Search> searchesToFilter);
}
