package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.GeographicalPositionFactory;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.spec.GeographicalPositionSpec;

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
