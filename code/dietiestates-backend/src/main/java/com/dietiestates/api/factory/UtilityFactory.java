package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.model.Utility;

public interface UtilityFactory {

	Utility createUtility(UtilityRequest request, Long detailId);
}
