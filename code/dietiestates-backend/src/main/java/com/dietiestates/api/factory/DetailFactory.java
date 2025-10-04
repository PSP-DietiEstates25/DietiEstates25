package com.dietiestates.api.factory;

import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.spec.DetailSpec;

public interface DetailFactory {

	Detail createDetailFromSpec(
			DetailSpec spec,
			RealEstate realEstate,
			Search search
			);
}
