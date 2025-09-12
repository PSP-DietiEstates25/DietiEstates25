package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.DetailsNotFoundException;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.DetailsRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final DetailsRepository detailsRepository;
	
	public RealEstate createRealEstate(RealEstateDto request) {
		var realEstate = of(request);
		realEstateRepository.save(realEstate);
		return realEstate;
	}
	
	public RealEstate of(RealEstateDto request) {
		var details = detailsRepository.findById(request.getDetailsId()).orElseThrow(DetailsNotFoundException::new);
		return RealEstate.builder()
				.category(AdCategory.valueOf(request.getCategory()))
				.images(request.getImages())
				.description(request.getDescription())
				.detail(details)
				.build();
	}
}
