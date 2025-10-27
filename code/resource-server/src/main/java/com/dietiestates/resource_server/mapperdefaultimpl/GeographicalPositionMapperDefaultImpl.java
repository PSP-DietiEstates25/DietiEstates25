package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.GeographicalPositionRequest;
import com.dietiestates.resource_server.dto.response.GeographicalPositionResponse;
import com.dietiestates.resource_server.mapper.GeographicalPositionMapper;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.spec.GeographicalPositionSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeographicalPositionMapperDefaultImpl implements GeographicalPositionMapper {

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
