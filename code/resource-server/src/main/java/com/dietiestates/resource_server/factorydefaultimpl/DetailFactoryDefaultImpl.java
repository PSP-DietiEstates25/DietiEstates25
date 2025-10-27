package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.DetailFactory;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.spec.DetailSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailFactoryDefaultImpl implements DetailFactory {

    @Override
    public Detail createDetailFromSpec(
            DetailSpec spec,
            GeographicalPosition geographicalPosition,
            Utility utility
    ) {
        return Detail.builder()
                .geographicalPosition(geographicalPosition)
                .utility(utility)
                .build();
    }
}
