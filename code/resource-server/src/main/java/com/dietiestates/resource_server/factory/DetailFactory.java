package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.spec.DetailSpec;

public interface DetailFactory {

    Detail createDetailFromSpec(
            DetailSpec spec,
            GeographicalPosition geographicalPosition,
            Utility utility
    );
}
