package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.exception.notfound.UtilityNotFoundException;
import com.dietiestates.api.exception.notowned.UtilityNotOwnedByDetailException;
import com.dietiestates.api.mapper.UtilityMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.UtilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityService {

	private final UtilityRepository utilityRepository;
	private final UtilityMapper utilityMapper;
	
	private final DetailService detailService;
	
	public Utility createUtility(UtilityRequest request, Long detailId) {
		
		var detail = detailService.getDetailById(detailId);
		var utility = utilityMapper.toEntity(request, detail);
		return utilityRepository.save(utility);
	}
	
	public UtilityResponse getUtility(Long detailId, Long utilityId) {
		
		var utility = this.getUtilityById(utilityId);
		var detail = detailService.getDetailById(detailId);
		
		this.checkUtilityOwnedByDetail(detail.getId() , utility.getDetail().getId());
		
		return utilityMapper.fromEntity(utility);
	}
	
	public Utility getUtilityById(Long id) {
		return utilityRepository.findById(id)
				.orElseThrow(UtilityNotFoundException::new);
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
	
	public void checkUtilityOwnedByDetail(Long detailId, Long utilityDetailId) {
		
		if(!detailId.equals(utilityDetailId))
			throw new UtilityNotOwnedByDetailException();
		
	}
}
