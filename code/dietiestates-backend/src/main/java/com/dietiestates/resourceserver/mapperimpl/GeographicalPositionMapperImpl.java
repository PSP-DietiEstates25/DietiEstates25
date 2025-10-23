package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.GeographicalPositionRequest;
import com.dietiestates.resourceserver.dto.response.GeographicalPositionResponse;
import com.dietiestates.resourceserver.mapper.GeographicalPositionMapper;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.spec.GeographicalPositionSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeographicalPositionMapperImpl implements GeographicalPositionMapper {

	@Override
	public GeographicalPositionSpec toSpec(GeographicalPositionRequest request) {
		return GeographicalPositionSpec.builder()
				.city(request.getCity())
				.municipality(request.getMunicipality())
				.address(request.getAddress())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.radius(request.getRadius())
				.build();
	}

	@Override
	public GeographicalPositionResponse fromEntity(GeographicalPosition geographicalPosition) {
		return GeographicalPositionResponse.builder()
				.id(geographicalPosition.getId())
				.createdDate(geographicalPosition.getCreatedDate())
				.lastModifiedDate(geographicalPosition.getLastModifiedDate())
				.city(geographicalPosition.getCity())
				.municipality(geographicalPosition.getMunicipality())
				.address(geographicalPosition.getAddress())
				.longitude(geographicalPosition.getLongitude())
				.latitude(geographicalPosition.getLatitude())
				.radius(geographicalPosition.getRadius())
				.detailId(geographicalPosition.getDetail() != null ? geographicalPosition.getDetail().getId() : null)
				.build();
	}

}
