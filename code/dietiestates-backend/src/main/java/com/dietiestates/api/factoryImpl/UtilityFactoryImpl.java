package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.UtilityFactory;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.UtilitySpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UtilityFactoryImpl implements UtilityFactory {

	@Override
	public Utility createUtilityFromSpec(
			UtilitySpec spec
			) {
		return Utility.builder()
				.hasAirConditioning(spec.getHasAirConditioning())
				.hasDoorman(spec.getHasDoorman())
				.hasElevator(spec.getHasElevator())
				.build();
	}

}
