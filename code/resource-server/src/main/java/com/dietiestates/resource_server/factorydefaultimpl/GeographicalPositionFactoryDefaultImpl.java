package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.GeographicalPositionFactory;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.spec.GeographicalPositionSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeographicalPositionFactoryDefaultImpl implements GeographicalPositionFactory {

    @Override
    public GeographicalPosition createGeographicalPositionFromSpec(
            GeographicalPositionSpec spec
    ) {
        return GeographicalPosition.builder()
                .city(spec.getCity())
                .municipality(spec.getMunicipality())
                .address(spec.getAddress())
                .latitude(spec.getLatitude())
                .longitude(spec.getLongitude())
                .radius(spec.getRadius())
                .build();
    }
}
