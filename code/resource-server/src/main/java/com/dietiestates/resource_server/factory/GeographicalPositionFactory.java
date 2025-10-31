package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.spec.GeographicalPositionSpec;

public interface GeographicalPositionFactory {

    GeographicalPosition createGeographicalPositionFromSpec(
            GeographicalPositionSpec spec
    );
}
