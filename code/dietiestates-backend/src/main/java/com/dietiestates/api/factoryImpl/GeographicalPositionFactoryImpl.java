package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.factory.GeographicalPositionFactory;
import com.dietiestates.api.model.GeographicalPosition;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeographicalPositionFactoryImpl implements GeographicalPositionFactory {

	@Override
	public GeographicalPosition createGeographicalPosition(GeographicalPositionRequest request, Long detailId) {
		return null;
	}
}
