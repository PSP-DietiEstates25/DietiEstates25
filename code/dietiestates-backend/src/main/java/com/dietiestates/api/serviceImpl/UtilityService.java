package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.factory.UtilityFactory;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.UtilityFinder;
import com.dietiestates.api.mapper.UtilityMapper;
import com.dietiestates.api.repository.UtilityRepository;
import com.dietiestates.api.verifier.UtilityVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityService {

	private final UtilityRepository utilityRepository;
	private final UtilityFactory utilityFactory;
	private final UtilityFinder utilityFinder;
	private final UtilityVerifier utilityVerifier;
	private final UtilityMapper utilityMapper;
	
	private final DetailFinder detailFinder;
	
	public void createUtility(UtilityRequest request, Long detailId) {
		
		var utilitySpec = utilityMapper.toSpec(request);
		
		var detail = detailFinder.getDetailById(detailId);
		
		var utility = utilityFactory.createUtilityFromSpec(utilitySpec, detail);
		utilityRepository.save(utility);
	}
	
	public UtilityResponse getUtility(Long detailId, Long utilityId) {
		
		var utility = utilityFinder.getUtilityById(utilityId);
		var detail = detailFinder.getDetailById(detailId);
		
		utilityVerifier.checkUtilityOwnedByDetail(
				utility.getDetail().getId(),
				detail.getId()
				);
		
		return utilityMapper.fromEntity(utility);
	}
}
