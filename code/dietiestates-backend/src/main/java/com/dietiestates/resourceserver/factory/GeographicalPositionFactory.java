package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.spec.GeographicalPositionSpec;

public interface GeographicalPositionFactory {

	GeographicalPosition createGeographicalPositionFromSpec(
			GeographicalPositionSpec spec
			);
}
