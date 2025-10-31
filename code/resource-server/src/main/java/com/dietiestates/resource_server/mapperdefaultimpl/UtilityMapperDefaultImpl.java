package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.UtilityRequest;
import com.dietiestates.resource_server.dto.response.UtilityResponse;
import com.dietiestates.resource_server.mapper.UtilityMapper;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.spec.UtilitySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilityMapperDefaultImpl implements UtilityMapper {

	@Override
	public UtilitySpec toSpec(UtilityRequest request) {
		return UtilitySpec.builder()
				.hasAirConditioning(request.getHasAirConditioning())
				.hasDoorman(request.getHasDoorman())
				.hasElevator(request.getHasElevator())
                .nearSchool(request.getNearSchool())
                .nearPublicTransport(request.getNearPublicTransport())
                .nearPark(request.getNearPark())
				.build();
	}

	@Override
	public UtilityResponse fromEntity(Utility utility) {
		return UtilityResponse.builder()
				.id(utility.getId())
				.createdDate(utility.getCreatedDate())
				.lastModifiedDate(utility.getLastModifiedDate())
				.hasAirConditioning(utility.getHasAirConditioning())
				.hasDoorman(utility.getHasDoorman())
				.hasElevator(utility.getHasElevator())
                .nearSchool(utility.getNearSchool())
                .nearPublicTransport(utility.getNearPublicTransport())
                .nearPark(utility.getNearPark())
				.detailId(utility.getDetail() != null ? utility.getDetail().getId() : null)
				.build();
	}
}
