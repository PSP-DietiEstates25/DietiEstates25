package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.CadastralDataDto;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.repository.CadastralDataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;
	
	public CadastralData createCadastralData(CadastralDataDto request) {
		var cadastralData = of(request);
		cadastralDataRepository.save(cadastralData);
		return cadastralData;
	}
	
	private CadastralData of(CadastralDataDto request) {
		return CadastralData.builder()
				.price(request.getPrice())
				.size(request.getSize())
				.energyClass(EnergyClass.valueOf(request.getEnergyClass()))
				.rooms(request.getRooms())
				.floor(request.getFloor())
				.build();
	}
}
