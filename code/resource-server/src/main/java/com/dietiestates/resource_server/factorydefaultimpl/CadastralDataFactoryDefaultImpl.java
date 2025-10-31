package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.CadastralDataFactory;
import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.spec.CadastralDataSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralDataFactoryDefaultImpl implements CadastralDataFactory {

    @Override
    public CadastralData createCadastralDataFromSpec(
            CadastralDataSpec spec
    ) {
        return CadastralData.builder()
                .price(spec.getPrice())
                .squareMeters(spec.getSquareMeters())
                .energyClass(spec.getEnergyClass())
                .rooms(spec.getRooms())
                .floor(spec.getFloor())
                .build();
    }
}
