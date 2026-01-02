package com.dietiestates.resource_server.filterdefaultimpl;

import com.dietiestates.resource_server.filter.SearchFilter;
import com.dietiestates.resource_server.model.*;
import com.dietiestates.resource_server.utils.MatchingUtils;
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
                .filter(search -> MatchingUtils.matchesUtilities(search.getDetail().getUtility(), utility))
                .toList();
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

    private static boolean isTrue(Boolean v) {
        return Boolean.TRUE.equals(v);
    }
}
