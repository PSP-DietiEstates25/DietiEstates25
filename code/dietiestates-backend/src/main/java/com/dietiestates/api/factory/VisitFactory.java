package com.dietiestates.api.factory;

import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.spec.VisitSpec;

public interface VisitFactory {

	Visit createVisitFromSpec(
			VisitSpec spec,
			User user,
			RealEstate realEstate
			);
}
