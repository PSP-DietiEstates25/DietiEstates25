package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.factory.UtilityFactory;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.UtilityFinder;
import com.dietiestates.api.mapper.UtilityMapper;
import com.dietiestates.api.repository.UtilityRepository;
import com.dietiestates.api.service.UtilityService;
import com.dietiestates.api.verifier.UtilityVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilityServiceImpl implements UtilityService {

	private final UtilityRepository utilityRepository;
	private final UtilityFactory utilityFactory;
	private final UtilityFinder utilityFinder;
	// private final UtilityVerifier utilityVerifier;
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

	@Override
	@Transactional
	public UtilityResponse updateUtility(Long id, UtilityRequest request) {
		var entity = utilityFinder.getUtilityById(id);

		if (request.getHasAirConditioning() != null)
			entity.setHasAirConditioning(request.getHasAirConditioning());
		if (request.getHasDoorman() != null)
			entity.setHasDoorman(request.getHasDoorman());
		if (request.getHasElevator() != null)
			entity.setHasElevator(request.getHasElevator());

		var saved = utilityRepository.save(entity);
		return utilityMapper.fromEntity(saved);
	}
}
