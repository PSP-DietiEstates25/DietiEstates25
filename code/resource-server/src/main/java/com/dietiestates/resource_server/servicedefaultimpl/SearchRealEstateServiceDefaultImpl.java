package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.factory.SearchRealEstateFactory;
import com.dietiestates.resource_server.filter.RealEstateFilter;
import com.dietiestates.resource_server.filter.SearchFilter;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.SearchFinder;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.SearchRealEstate;
import com.dietiestates.resource_server.service.SearchRealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRealEstateServiceDefaultImpl implements SearchRealEstateService {

    private final SearchRealEstateFactory searchRealEstateFactory;

    private final RealEstateFinder realEstateFinder;
    private final SearchFinder searchFinder;

    private final RealEstateFilter realEstateFilter;
    private final SearchFilter searchFilter;

	@Override
	public void createSearchRealEstate(Search search, List<RealEstate> realEstates) {
		
		var searchRealEstates = new ArrayList<SearchRealEstate>();
		
		realEstates.forEach(realEstate -> {
			var searchRealEstate = searchRealEstateFactory.createSearchRealEstate(search, realEstate);
			searchRealEstates.add(searchRealEstate);
		});
		
	}

    @Override
    public void createRealEstateSearch(RealEstate realEstate, List<Search> searches){

        var realEstateSearches = new ArrayList<SearchRealEstate>();

        searches.forEach(search -> {
            var searchRealEstate = searchRealEstateFactory.createSearchRealEstate(search, realEstate);
            realEstateSearches.add(searchRealEstate);
        });
    }

    @Override
    public List<RealEstate> getSearchedRealEstates(Search search) {

        var searchedRealEstates = getRealEstatesBySearchFilter(search);

        if(!searchedRealEstates.isEmpty())
            createSearchRealEstate(search, searchedRealEstates);

        return searchedRealEstates;
    }

    @Override
    public List<Search> createRealEstateSearchesLink(RealEstate realEstate){

        var realEstateSearches = getSearchesByRealEstateFilter(realEstate);

        if(!realEstateSearches.isEmpty())
            createRealEstateSearch(realEstate, realEstateSearches);

        return realEstateSearches;
    }

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

        var searchesFilteredByCadastralData = searchFilter.filtlerSearchesByCadastralData(
                realEstate.getCadastralData(),
                searchesFilteredByUtility
        );

        return searchesFilteredByCadastralData;
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

        var realEstatesByCadastralFilter = realEstateFilter.filterRealEstateByCadastralFilters(
                search.getCadastralFilter(),
                realEstatesByUtility
        );

        return realEstatesByCadastralFilter;
    }

}