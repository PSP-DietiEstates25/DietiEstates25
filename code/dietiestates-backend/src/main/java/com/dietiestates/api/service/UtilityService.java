package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.UtilityDto;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.UtilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityService {

	private final UtilityRepository utilityRepository;
	
	public Utility createUtility(UtilityDto request) {
		var utility = of(request);
		utilityRepository.save(utility);
		return utility;
	}
	
	private Utility of(UtilityDto request) {
		return Utility.builder()
				.hasAirConditioning(request.isHasAirConditioning())
				.hasDoorman(request.isHasDoorman())
				.hasElevator(request.isHasElevator())
				.build();
	}
}
