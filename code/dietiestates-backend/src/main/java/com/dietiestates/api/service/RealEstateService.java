package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final EstateAgentRepository estateAgentRepository;
	
	public void createRealEstate(RealEstateDto request) {
		var realEstate = of(request);
		realEstateRepository.save(realEstate);
	}
	
	public RealEstate of(RealEstateDto request) {
		
		var estateAgent = estateAgentRepository.findByEmail(request.getEstateAgentEmail())
				.orElseThrow(EstateAgentNotFoundException::new);
		
		return RealEstate.builder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.images(request.getImages())
				.description(request.getDescription())
				.estateAgent(estateAgent)
				.build();
	}
	
	public List<RealEstate> getAllRealEstates(){
		
		var realEstatesIterable = realEstateRepository.findAll();
		var allRealEstates = new ArrayList<RealEstate>();
		realEstatesIterable.forEach(allRealEstates::add);
		
		return allRealEstates;
	}
}
