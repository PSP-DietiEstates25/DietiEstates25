package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.DetailFactory;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.DetailSpec;

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
