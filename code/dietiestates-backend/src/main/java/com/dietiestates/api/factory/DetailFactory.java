package com.dietiestates.api.factory;

import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.DetailSpec;

public interface DetailFactory {

	Detail createDetailFromSpec(
			DetailSpec spec,
			GeographicalPosition geographicalPosition,
			Utility utility
			);
}
