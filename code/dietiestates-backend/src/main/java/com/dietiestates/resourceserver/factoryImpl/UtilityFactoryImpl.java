package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.UtilityFactory;
import com.dietiestates.resourceserver.model.Utility;
import com.dietiestates.resourceserver.spec.UtilitySpec;

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
