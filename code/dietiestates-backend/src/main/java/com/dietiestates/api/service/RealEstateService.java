package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final DetailRepository detailRepository;
	private final EstateAgentRepository estateAgentRepository;
	
	public void createRealEstate(RealEstateDto request) {
		var realEstate = of(request);
		realEstateRepository.save(realEstate);
	}
	
	public RealEstate of(RealEstateDto request) {
		
		var details = detailRepository.findById(request.getDetailsId())
				.orElseThrow(DetailNotFoundException::new);
		
		var estateAgent = estateAgentRepository.findByEmail(request.getEstateAgentEmail())
				.orElseThrow(EstateAgentNotFoundException::new);
		
		return RealEstate.realEstateBuilder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.images(request.getImages())
				.description(request.getDescription())
				.estateAgent(estateAgent)
				.detail(details)
				.build();
	}
}
