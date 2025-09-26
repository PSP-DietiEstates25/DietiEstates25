package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.CadastralDataDto;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.repository.CadastralDataRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;
	private final RealEstateRepository realEstateRepository;
	
	public CadastralData createCadastralData(CadastralDataDto request, Long realEstateId) {
		var cadastralData = of(request, realEstateId);
		cadastralDataRepository.save(cadastralData);
		return cadastralData;
	}
	
	public CadastralData of(CadastralDataDto request, Long realEstateId) {
		
		var realEstate = realEstateRepository.findById(realEstateId)
				.orElseThrow(RealEstateNotFoundException::new);
		
		return CadastralData.cadastralDataBuilder()
				.createdDate(LocalDateTime.now())
				.price(request.getPrice())
				.squareMeters(request.getSquareMeters())
				.energyClass(request.getEnergyClass())
				.rooms(request.getRooms())
				.floor(request.getFloor())
				.realEstate(realEstate)
				.build();
	}
}
