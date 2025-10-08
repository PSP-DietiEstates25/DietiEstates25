package com.dietiestates.api.factory;

import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.UtilitySpec;

public interface UtilityFactory {

	Utility createUtilityFromSpec(
			UtilitySpec spec
			);
}
