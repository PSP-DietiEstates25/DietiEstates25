package com.dietiestates.resource_server.filterdefaultimpl;

import com.dietiestates.resource_server.filter.RealEstateFilter;
import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.utils.MatchingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RealEstateFilterDefaultImpl implements RealEstateFilter {

    @Override
    public List<RealEstate> filterRealEstateByGeographicalPosition(GeographicalPosition geographicalPosition, List<RealEstate> realEstatesToFilter) {

        var realEstatesFilteredByGeographicalPosition = new ArrayList<RealEstate>();

        realEstatesToFilter.forEach(realEstate -> {

            var realEstateGeographicalPosition = realEstate.getDetail().getGeographicalPosition();
            if(
                    realEstateGeographicalPosition.getCity().equals(geographicalPosition.getCity()) &&
                            realEstateGeographicalPosition.getMunicipality().equals(geographicalPosition.getMunicipality()) &&
                            realEstateGeographicalPosition.getRegion().equals(geographicalPosition.getRegion())
            )
                realEstatesFilteredByGeographicalPosition.add(realEstate);
        });

        return realEstatesFilteredByGeographicalPosition;
    }

    @Override
    public List<RealEstate> filterRealEstateByUtility(Utility utility, List<RealEstate> realEstatesToFilter) {

        return realEstatesToFilter.stream()
                .filter(realEstate -> MatchingUtils.matchesUtilities(utility, realEstate.getDetail().getUtility()))
                .toList();
    }

    @Override
    public List<RealEstate> filterRealEstateByCadastralFilters(CadastralFilter cadastralFilter, List<RealEstate> realEstatesToFilter) {

        var realEstatesFilteredByCadastralFilter = new ArrayList<RealEstate>();

        realEstatesToFilter.forEach(realEstate -> {

            var realEstateCadastralData = realEstate.getCadastralData();
            if(
                    cadastralFilter.getPriceRange().contains(realEstateCadastralData.getPrice()) &&
                            cadastralFilter.getSquareMetersRange().contains(realEstateCadastralData.getSquareMeters()) &&
                            cadastralFilter.getEnergyClassRange().contains(realEstateCadastralData.getEnergyClass().getOrder()) &&
                            cadastralFilter.getRoomsRange().contains(realEstateCadastralData.getRooms()) &&
                            cadastralFilter.getFloorRange().contains(realEstateCadastralData.getFloor())
            )
                realEstatesFilteredByCadastralFilter.add(realEstate);
        });

        return realEstatesFilteredByCadastralFilter;
    }
}
