package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.CadastralFilterFactory;
import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.spec.CadastralFilterSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralFilterFactoryDefaultImpl implements CadastralFilterFactory {

    @Override
    public CadastralFilter createCadastralFilterFromSpec(
            CadastralFilterSpec spec
    ) {
        return CadastralFilter.builder()
                .minPrice(spec.getMinPrice())
                .maxPrice(spec.getMaxPrice())
                .minSquareMeters(spec.getMinSquareMeters())
                .maxSquareMeters(spec.getMaxSquareMeters())
                .minEnergyClass(spec.getMinEnergyClass())
                .maxEnergyClass(spec.getMaxEnergyClass())
                .minRooms(spec.getMinRooms())
                .maxRooms(spec.getMaxRooms())
                .minFloor(spec.getMinFloor())
                .maxFloor(spec.getMaxFloor())
                .build();
    }
}
