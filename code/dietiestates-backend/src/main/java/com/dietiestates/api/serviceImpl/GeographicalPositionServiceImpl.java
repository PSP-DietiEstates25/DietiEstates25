package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	// private final GeographicalPositionVerifier geographicalPositionVerifier;
	private final GeographicalPositionMapper geographicalPositionMapper;

	@Override
	public GeographicalPositionResponse createGeographicalPosition(GeographicalPositionRequest request) {

		var geographicalPositionSpec = geographicalPositionMapper.toSpec(request);

		var geographicalPosition = geographicalPositionFactory
				.createGeographicalPositionFromSpec(geographicalPositionSpec);
		geographicalPositionRepository.save(geographicalPosition);

		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}

	@Override
	public GeographicalPositionResponse getGeographicalPositionById(Long geographicalPositionId) {

		var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(geographicalPositionId);

		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}

	@Override
	@Transactional
	public GeographicalPositionResponse updateGeographicalPosition(Long id, GeographicalPositionRequest request) {
		var entity = geographicalPositionFinder.getGeographicalPositionById(id);

		if (request.getAddress() != null)
			entity.setAddress(request.getAddress());
		if (request.getCity() != null)
			entity.setCity(request.getCity());
		if (request.getMunicipality() != null)
			entity.setMunicipality(request.getMunicipality());
		if (request.getLatitude() != null)
			entity.setLatitude(request.getLatitude());
		if (request.getLongitude() != null)
			entity.setLongitude(request.getLongitude());
		if (request.getRadius() != null)
			entity.setRadius(request.getRadius());

		var saved = geographicalPositionRepository.save(entity);
		return geographicalPositionMapper.fromEntity(saved);
	}

}
