package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.mapper.RealEstateMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final RealEstateMapper realEstateMapper;
	private final EstateAgentRepository estateAgentRepository;
	
	public void createRealEstate(RealEstateRequest request) {
		
		var estateAgent = estateAgentRepository.findByEmail(request.getEstateAgentEmail())
				.orElseThrow(EstateAgentNotFoundException::new);
		
		var realEstate = realEstateMapper.toEntity(request, estateAgent);
		
		realEstateRepository.save(realEstate);
	}
	
	public List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates) {
		
		var response = new ArrayList<RealEstateResponse>();
		
		realEstates.forEach(realEstate -> {
			var realEstateResponse = realEstateMapper.fromEntity(realEstate);
			response.add(realEstateResponse);
		});
		
		return response;
	}
	
	public RealEstate getRealEstateById(Long id) {
		return realEstateRepository.findById(id)
				.orElseThrow(RealEstateNotFoundException::new);
	}
	
	public List<RealEstate> getAllRealEstates(){
		
		var realEstatesIterable = realEstateRepository.findAll();
		var allRealEstates = new ArrayList<RealEstate>();
		realEstatesIterable.forEach(allRealEstates::add);
		
		return allRealEstates;
	}
}
