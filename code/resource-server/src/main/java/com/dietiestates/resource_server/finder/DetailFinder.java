package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.DetailNotFoundException;
import com.dietiestates.resource_server.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resource_server.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.Utility;

public interface DetailFinder {
	Detail getDetailById(Long id) throws DetailNotFoundException;
    Detail getGeographicalPositionDetail(Long geographicalPositionId) throws DetailNotFoundException;
    Detail getUtilityDetail(Long utilityId) throws DetailNotFoundException;
}
