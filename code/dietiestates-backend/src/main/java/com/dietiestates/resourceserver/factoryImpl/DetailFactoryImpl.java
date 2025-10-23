package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.DetailFactory;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.model.Utility;
import com.dietiestates.resourceserver.spec.DetailSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailFactoryImpl implements DetailFactory {

	@Override
	public Detail createDetailFromSpec(
			DetailSpec spec,
			GeographicalPosition geographicalPosition,
			Utility utility
			) {
		return Detail.builder()
				.geographicalPosition(geographicalPosition)
				.utility(utility)
				.build();
	}
}
