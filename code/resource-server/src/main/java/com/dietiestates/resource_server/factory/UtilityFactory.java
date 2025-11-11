package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.spec.UtilitySpec;

public interface UtilityFactory {
    Utility createUtilityFromSpec(UtilitySpec spec);
}
