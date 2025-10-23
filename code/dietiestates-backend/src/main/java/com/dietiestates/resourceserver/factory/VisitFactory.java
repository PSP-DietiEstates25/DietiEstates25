package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.model.Visit;
import com.dietiestates.resourceserver.spec.VisitSpec;

public interface VisitFactory {

	Visit createVisitFromSpec(
			VisitSpec spec,
			User user,
			RealEstate realEstate
			);
}
