package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.GeographicalPosition;

@Component
public class GeographicalPositionMapper {

	public GeographicalPosition toEntity(GeographicalPositionRequest request, Detail detail) {
		return GeographicalPosition.geographicalPositionBuilder()
				.createdDate(LocalDateTime.now())
				.city(request.getCity())
				.municipality(request.getMunicipality())
				.address(request.getAddress())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.radius(request.getRadius())
				.detail(detail)
				.build();
	}
	
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
				.detailId(geographicalPosition.getDetail().getId())
				.build();
	}
}
