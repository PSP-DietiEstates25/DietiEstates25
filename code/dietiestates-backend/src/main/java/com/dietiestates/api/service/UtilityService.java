package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.mapper.UtilityMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.UtilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityService {

	private final UtilityRepository utilityRepository;
	private final UtilityMapper utilityMapper;
	private final DetailRepository detailRepository;
	
	public Utility createUtility(UtilityRequest request, Long detailId) {
		
		var detail = detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
		
		var utility = utilityMapper.toEntity(request, detail);
		
		return utilityRepository.save(utility);
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
