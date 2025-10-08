package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.CadastralDataFactory;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.spec.CadastralDataSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralDataFactoryImpl implements CadastralDataFactory {

	@Override
	public CadastralData createCadastralDataFromSpec(
			CadastralDataSpec spec
			) {
		return CadastralData.builder()
				.price(spec.getPrice())
				.squareMeters(spec.getSquareMeters())
				.energyClass(spec.getEnergyClass())
				.rooms(spec.getRooms())
				.floor(spec.getFloor())
				.build();
	}
}
