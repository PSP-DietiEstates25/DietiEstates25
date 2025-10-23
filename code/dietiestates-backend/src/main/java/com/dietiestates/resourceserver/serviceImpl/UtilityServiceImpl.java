package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.UtilityRequest;
import com.dietiestates.resourceserver.dto.response.UtilityResponse;
import com.dietiestates.resourceserver.factory.UtilityFactory;
import com.dietiestates.resourceserver.finder.DetailFinder;
import com.dietiestates.resourceserver.finder.UtilityFinder;
import com.dietiestates.resourceserver.mapper.UtilityMapper;
import com.dietiestates.resourceserver.repository.UtilityRepository;
import com.dietiestates.resourceserver.service.UtilityService;
import com.dietiestates.resourceserver.verifier.UtilityVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityServiceImpl implements UtilityService {

	private final UtilityRepository utilityRepository;
	private final UtilityFactory utilityFactory;
	private final UtilityFinder utilityFinder;
	//private final UtilityVerifier utilityVerifier;
	private final UtilityMapper utilityMapper;
	
	@Override
	public UtilityResponse createUtility(UtilityRequest request) {
		
		var utilitySpec = utilityMapper.toSpec(request);
		
		var utility = utilityFactory.createUtilityFromSpec(utilitySpec);
		utilityRepository.save(utility);
		
		return utilityMapper.fromEntity(utility);
	}
	
	@Override
	public UtilityResponse getUtilityById(Long utilityId) {
		
		var utility = utilityFinder.getUtilityById(utilityId);
		
		return utilityMapper.fromEntity(utility);
	}
}
