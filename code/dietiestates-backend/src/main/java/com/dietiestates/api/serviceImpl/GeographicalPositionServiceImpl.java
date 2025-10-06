package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.factory.GeographicalPositionFactory;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.GeographicalPositionFinder;
import com.dietiestates.api.mapper.GeographicalPositionMapper;
import com.dietiestates.api.repository.GeographicalPositionRepository;
import com.dietiestates.api.verifier.GeographicalPositionVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	private final GeographicalPositionFactory geographicalPositionFactory;
	private final GeographicalPositionFinder geographicalPositionFinder;
	private final GeographicalPositionVerifier geographicalPositionVerifier;
	private final GeographicalPositionMapper geographicalPositionMapper;
	
	private final DetailFinder detailFinder;
	
	public void createGeographicalPosition(GeographicalPositionRequest request, Long detailId) {
		
		var geographicalPositionSpec = geographicalPositionMapper.toSpec(request);
		
		var detail = detailFinder.getDetailById(detailId);

		var geographicalPosition = geographicalPositionFactory.createGeographicalPositionFromSpec(geographicalPositionSpec, detail);
		geographicalPositionRepository.save(geographicalPosition);
	}
	
	public GeographicalPositionResponse getGeographicalPosition(Long detailId, Long geographicalPositionId) {
		
		var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(geographicalPositionId);
		var detail = detailFinder.getDetailById(detailId);
		
		geographicalPositionVerifier.checkGeographicalPositionOwnedByDetail(
				geographicalPosition.getDetail().getId(),
				detail.getId()
				);
		
		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}
	
}
