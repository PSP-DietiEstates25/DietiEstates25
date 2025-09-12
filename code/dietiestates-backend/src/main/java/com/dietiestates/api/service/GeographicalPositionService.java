package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.GeographicalPositionDto;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.repository.GeographicalPositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	
	public GeographicalPosition createGeographicalPosition(GeographicalPositionDto request) {
		var geographicalPosition = of(request);
		geographicalPositionRepository.save(geographicalPosition);
		return geographicalPosition;
	}
	
	private GeographicalPosition of(GeographicalPositionDto request) {
		return GeographicalPosition.builder()
				.city(request.getCity())
				.municipality(request.getMunicipality())
				.address(request.getAddress())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.radius(request.getRadius())
				.build();
	}
}
