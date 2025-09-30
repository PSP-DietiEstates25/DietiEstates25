package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.mapper.CadastralDataMapper;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.repository.CadastralDataRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;
	private final CadastralDataMapper cadastralDataMapper;
	private final RealEstateRepository realEstateRepository;
	
	public CadastralData createCadastralData(CadastralDataRequest request, Long realEstateId) {
		
		var realEstate = realEstateRepository.findById(realEstateId)
				.orElseThrow(RealEstateNotFoundException::new);
		
		var cadastralData = cadastralDataMapper.toEntity(request, realEstate);
		
		return cadastralDataRepository.save(cadastralData);
	}
}
