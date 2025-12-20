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

        return searchesToFilter.stream()
                .filter(search -> matchesUtilities(search.getDetail().getUtility(), utility))
                .toList();
    }

    @Override
    public boolean matchesUtilities(Utility searchUtility, Utility realEstateUtility){

        if(Boolean.TRUE.equals(searchUtility.getHasDoorman()) && !Boolean.TRUE.equals(realEstateUtility.getHasDoorman()))
            return false;

        if(Boolean.TRUE.equals(searchUtility.getHasElevator()) && !Boolean.TRUE.equals(realEstateUtility.getHasElevator()))
            return false;

        if(Boolean.TRUE.equals(searchUtility.getHasAirConditioning()) && !Boolean.TRUE.equals(realEstateUtility.getHasAirConditioning()))
            return false;

        if(Boolean.TRUE.equals(searchUtility.getNearPark()) && !Boolean.TRUE.equals(realEstateUtility.getNearPark()))
            return false;

        if(Boolean.TRUE.equals(searchUtility.getNearSchool()) && !Boolean.TRUE.equals(realEstateUtility.getNearSchool()))
            return false;

        if(Boolean.TRUE.equals(searchUtility.getNearPublicTransport()) && !Boolean.TRUE.equals(realEstateUtility.getNearPublicTransport()))
            return false;

        return true;
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
