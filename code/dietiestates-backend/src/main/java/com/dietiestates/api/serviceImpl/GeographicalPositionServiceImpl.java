package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.factory.GeographicalPositionFactory;
import com.dietiestates.api.finder.GeographicalPositionFinder;
import com.dietiestates.api.mapper.GeographicalPositionMapper;
import com.dietiestates.api.repository.GeographicalPositionRepository;
import com.dietiestates.api.service.GeographicalPositionService;
import com.dietiestates.api.verifier.GeographicalPositionVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicalPositionServiceImpl implements GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	private final GeographicalPositionFactory geographicalPositionFactory;
	private final GeographicalPositionFinder geographicalPositionFinder;
	//private final GeographicalPositionVerifier geographicalPositionVerifier;
	private final GeographicalPositionMapper geographicalPositionMapper;
	
	@Override
	public GeographicalPositionResponse createGeographicalPosition(GeographicalPositionRequest request) {
		
		var geographicalPositionSpec = geographicalPositionMapper.toSpec(request);

		var geographicalPosition = geographicalPositionFactory.createGeographicalPositionFromSpec(geographicalPositionSpec);
		geographicalPositionRepository.save(geographicalPosition);
		
		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}
	
	@Override
	public GeographicalPositionResponse getGeographicalPositionById(Long geographicalPositionId) {
		
		var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(geographicalPositionId);
		
		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}
	
}
