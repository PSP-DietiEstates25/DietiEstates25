package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.model.Utility;
import com.dietiestates.resourceserver.spec.DetailSpec;

public interface DetailFactory {

	Detail createDetailFromSpec(
			DetailSpec spec,
			GeographicalPosition geographicalPosition,
			Utility utility
			);
}
