package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.UtilityDto;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.UtilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityService {

	private final UtilityRepository utilityRepository;
	private final DetailRepository detailRepository;
	
	public Utility createUtility(UtilityDto request, Long detailId) {
		var utility = of(request, detailId);
		utilityRepository.save(utility);
		return utility;
	}
	
	public Utility of(UtilityDto request, Long detailId) {
		
		var detail = detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
				
		return Utility.utilityBuilder()
				.hasAirConditioning(request.isHasAirConditioning())
				.hasDoorman(request.isHasDoorman())
				.hasElevator(request.isHasElevator())
				.detail(detail)
				.build();
	}
}
