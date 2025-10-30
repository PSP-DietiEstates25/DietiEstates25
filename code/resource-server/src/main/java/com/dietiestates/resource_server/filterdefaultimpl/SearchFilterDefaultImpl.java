package com.dietiestates.resource_server.filterdefaultimpl;

import com.dietiestates.resource_server.filter.SearchFilter;
import com.dietiestates.resource_server.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchFilterDefaultImpl implements SearchFilter {

    @Override
    public List<Search> filterSearchesByGeographicalPosition(GeographicalPosition geographicalPosition, List<Search> searchesToFilter) {

        var searchesFilteredByGeographicalPosition = new ArrayList<Search>();

        searchesToFilter.forEach(search -> {

            var searchGeographicalPosition = search.getDetail().getGeographicalPosition();
            if(
                    searchGeographicalPosition.getCity().equals(geographicalPosition.getCity()) ||
                            searchGeographicalPosition.getMunicipality().equals(geographicalPosition.getMunicipality())
            )
                searchesFilteredByGeographicalPosition.add(search);

        });

        return searchesFilteredByGeographicalPosition;
    }

    @Override
    public List<Search> filterSearchesByUtility(Utility utility, List<Search> searchesToFilter) {

        var searchesFilteredByUtility = new ArrayList<Search>();

        searchesToFilter.forEach(search -> {

            var searchUtility = search.getDetail().getUtility();
            if(
                    searchUtility.getHasAirConditioning().equals(utility.getHasAirConditioning()) &&
                            searchUtility.getHasDoorman().equals(utility.getHasDoorman()) &&
                            searchUtility.getHasElevator().equals(utility.getHasElevator())
            )
                searchesFilteredByUtility.add(search);
        });

        return searchesFilteredByUtility;
    }

    @Override
    public List<Search> filtlerSearchesByCadastralData(CadastralData cadastralData, List<Search> searchesToFilter) {

        var searchesFilteredByCadastralData = new ArrayList<Search>();

        searchesToFilter.forEach(search -> {

            var searchCadastralFilter = search.getCadastralFilter();
            if(
                    searchCadastralFilter.getPriceRange().contains(cadastralData.getPrice()) &&
                            searchCadastralFilter.getSquareMetersRange().contains(cadastralData.getSquareMeters()) &&
                            searchCadastralFilter.getEnergyClassRange().contains(cadastralData.getEnergyClass().getOrder()) &&
                            searchCadastralFilter.getRoomsRange().contains(cadastralData.getRooms()) &&
                            searchCadastralFilter.getFloorRange().contains(cadastralData.getFloor())
            )
                searchesFilteredByCadastralData.add(search);
        });

        return searchesFilteredByCadastralData;
    }
}
