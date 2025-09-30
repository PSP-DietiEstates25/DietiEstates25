package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralDataRequest;
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
	
	public CadastralData createCadastralData(CadastralDataRequest request, Long realEstateId) {
		var cadastralData = of(request, realEstateId);
		return cadastralDataRepository.save(cadastralData);
	}
	
	public CadastralData of(CadastralDataRequest request, Long realEstateId) {
		
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
