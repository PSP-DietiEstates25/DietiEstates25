package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.CadastralDataFactory;
import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.spec.CadastralDataSpec;

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
