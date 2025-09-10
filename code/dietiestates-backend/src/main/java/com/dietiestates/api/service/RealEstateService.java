package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;

	public RealEstate createRealEstate(RealEstateDto request) {
		var realEstate = RealEstate.of(request);
		realEstateRepository.save(realEstate);
		return realEstate;
	}
}
