package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;
import com.dietiestates.resource_server.factory.RealEstateFactory;
import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.spec.RealEstateSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateFactoryDefaultImpl implements RealEstateFactory {

    @Override
    public RealEstate createRealEstateFromSpec(
            RealEstateSpec spec,
            EstateAgent estateAgent,
            CadastralData cadastralData,
            Detail detail
    ) {
        return RealEstate.builder()
                .category(spec.getCategory())
                .images(spec.getImages())
                .description(spec.getDescription())
                .estateAgent(estateAgent)
                .cadastralData(cadastralData)
                .detail(detail)
                .build();
    }

}
