package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;
import com.dietiestates.resource_server.factory.UtilityFactory;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.spec.UtilitySpec;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UtilityFactoryDefaultImpl implements UtilityFactory {

    @Override
    public Utility createUtilityFromSpec(
            UtilitySpec spec
    ) {
        return Utility.builder()
                .hasAirConditioning(spec.getHasAirConditioning())
                .hasDoorman(spec.getHasDoorman())
                .hasElevator(spec.getHasElevator())
                .nearPark(spec.getNearPark())
                .nearSchool(spec.getNearSchool())
                .nearPublicTransport(spec.getNearPublicTransport())
                .build();
    }

}
