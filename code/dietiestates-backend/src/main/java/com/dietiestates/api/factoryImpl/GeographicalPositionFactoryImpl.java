package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.GeographicalPositionFactory;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.spec.GeographicalPositionSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeographicalPositionFactoryImpl implements GeographicalPositionFactory {

	@Override
	public GeographicalPosition createGeographicalPositionFromSpec(
			GeographicalPositionSpec spec
			) {
		return GeographicalPosition.builder()
				.city(spec.getCity())
				.municipality(spec.getMunicipality())
				.address(spec.getAddress())
				.latitude(spec.getLatitude())
				.longitude(spec.getLongitude())
				.radius(spec.getRadius())
				.build();
	}
}
