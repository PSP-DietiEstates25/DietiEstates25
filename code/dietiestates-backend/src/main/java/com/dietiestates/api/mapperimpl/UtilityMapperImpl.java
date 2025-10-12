package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.mapper.UtilityMapper;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.UtilitySpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UtilityMapperImpl implements UtilityMapper {

	@Override
	public UtilitySpec toSpec(UtilityRequest request) {
		return UtilitySpec.builder()
				.hasAirConditioning(request.getHasAirConditioning())
				.hasDoorman(request.getHasDoorman())
				.hasElevator(request.getHasElevator())
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
				.detailId(utility.getDetail() != null ? utility.getDetail().getId() : null)
				.build();
	}
}
