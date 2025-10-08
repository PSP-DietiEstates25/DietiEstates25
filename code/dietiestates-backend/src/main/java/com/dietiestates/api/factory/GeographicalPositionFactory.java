package com.dietiestates.api.factory;

import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.spec.GeographicalPositionSpec;

public interface GeographicalPositionFactory {

	GeographicalPosition createGeographicalPositionFromSpec(
			GeographicalPositionSpec spec
			);
}
