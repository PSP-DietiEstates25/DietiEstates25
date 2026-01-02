package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.filter.RealEstateFilter;
import com.dietiestates.resource_server.filter.SearchFilter;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.SearchFinder;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.service.SearchRealEstateMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRealEstateMatchingServiceDefaultImpl implements SearchRealEstateMatchingService {

    private final SearchFinder searchFinder;
    private final RealEstateFinder realEstateFinder;

    private final SearchFilter searchFilter;
    private final RealEstateFilter realEstateFilter;

    @Override
    public List<Search> getSearchesByRealEstateFilter(RealEstate realEstate){
        var allSearches = searchFinder.getAllSearches();

        var searchesFilteredByGeographicalPosition = searchFilter.filterSearchesByGeographicalPosition(
                realEstate.getDetail().getGeographicalPosition(),
                allSearches
        );

        var searchesFilteredByUtility = searchFilter.filterSearchesByUtility(
                realEstate.getDetail().getUtility(),
                searchesFilteredByGeographicalPosition
        );

        return searchFilter.filtlerSearchesByCadastralData(
                realEstate.getCadastralData(),
                searchesFilteredByUtility
        );
    }

    @Override
    public List<RealEstate> getRealEstatesBySearchFilter(Search search){
        var allRealEstates = realEstateFinder.getAllRealEstates();

        var realEstatesFilteredByGeographicalPosition = realEstateFilter.filterRealEstateByGeographicalPosition(
                search.getDetail().getGeographicalPosition(),
                allRealEstates
        );

        var realEstatesByUtility = realEstateFilter.filterRealEstateByUtility(
                search.getDetail().getUtility(),
                realEstatesFilteredByGeographicalPosition
        );

        return realEstateFilter.filterRealEstateByCadastralFilters(
                search.getCadastralFilter(),
                realEstatesByUtility
        );
    }
}
