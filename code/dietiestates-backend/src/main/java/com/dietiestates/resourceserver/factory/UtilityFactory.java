package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Utility;
import com.dietiestates.resourceserver.spec.UtilitySpec;

public interface UtilityFactory {

	Utility createUtilityFromSpec(
			UtilitySpec spec
			);
}
