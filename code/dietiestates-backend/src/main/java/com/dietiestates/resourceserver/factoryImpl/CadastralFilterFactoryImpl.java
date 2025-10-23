package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.CadastralFilterFactory;
import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.spec.CadastralFilterSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralFilterFactoryImpl implements CadastralFilterFactory {

	@Override
	public CadastralFilter createCadastralFilterFromSpec(
			CadastralFilterSpec spec 
			) {
		return CadastralFilter.builder()
				.minPrice(spec.getMinPrice())
				.maxPrice(spec.getMaxPrice())
				.minSquareMeters(spec.getMinSquareMeters())
				.maxSquareMeters(spec.getMaxSquareMeters())
				.minEnergyClass(spec.getMinEnergyClass())
				.maxEnergyClass(spec.getMaxEnergyClass())
				.minRooms(spec.getMinRooms())
				.maxRooms(spec.getMaxRooms())
				.minFloor(spec.getMinFloor())
				.maxFloor(spec.getMaxFloor())
				.build();
	}
}
