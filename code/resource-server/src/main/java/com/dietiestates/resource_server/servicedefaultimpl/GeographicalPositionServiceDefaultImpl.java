package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.GeographicalPositionRequest;
import com.dietiestates.resource_server.dto.response.GeographicalPositionResponse;
import com.dietiestates.resource_server.factory.GeographicalPositionFactory;
import com.dietiestates.resource_server.finder.GeographicalPositionFinder;
import com.dietiestates.resource_server.mapper.GeographicalPositionMapper;
import com.dietiestates.resource_server.repository.GeographicalPositionRepository;
import com.dietiestates.resource_server.service.GeographicalPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeographicalPositionServiceDefaultImpl implements GeographicalPositionService {

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
