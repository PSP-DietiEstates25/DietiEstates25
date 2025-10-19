package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.GeographicalPositionRequest;
import com.dietiestates.resourceserver.dto.response.GeographicalPositionResponse;
import com.dietiestates.resourceserver.factory.GeographicalPositionFactory;
import com.dietiestates.resourceserver.finder.GeographicalPositionFinder;
import com.dietiestates.resourceserver.mapper.GeographicalPositionMapper;
import com.dietiestates.resourceserver.repository.GeographicalPositionRepository;
import com.dietiestates.resourceserver.service.GeographicalPositionService;
import com.dietiestates.resourceserver.verifier.GeographicalPositionVerifier;

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
