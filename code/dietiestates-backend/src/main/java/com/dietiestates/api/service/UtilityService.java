package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.UtilityDto;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.model.RealEstate;
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
		return utilityRepository.save(utility);
	}
	
	public Utility of(UtilityDto request, Long detailId) {
		
		var detail = detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
				
		return Utility.utilityBuilder()
				.hasAirConditioning(request.getHasAirConditioning())
				.hasDoorman(request.getHasDoorman())
				.hasElevator(request.getHasElevator())
				.detail(detail)
				.build();
	}
	
	public List<RealEstate> getUtilityRealEstates(Utility searchUtility, List<RealEstate> realEstates){
		var utilityRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateUtility = realEstate.getDetail().getUtility();
			if(
					realEstateUtility.getHasAirConditioning().equals(searchUtility.getHasAirConditioning()) &&
					realEstateUtility.getHasDoorman().equals(searchUtility.getHasDoorman()) &&
					realEstateUtility.getHasElevator().equals(searchUtility.getHasElevator())
				)
				utilityRealEstates.add(realEstate);
		});
		
		return utilityRealEstates;
	}
}
