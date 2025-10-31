package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.spec.RealEstateSpec;

public interface RealEstateFactory {

    RealEstate createRealEstateFromSpec(
            RealEstateSpec spec,
            EstateAgent estateAgent,
            CadastralData cadastralData,
            Detail detail
    );

}
