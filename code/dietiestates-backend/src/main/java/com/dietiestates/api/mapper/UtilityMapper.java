package com.dietiestates.api.mapper;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.UtilitySpec;

@Component
public class UtilityMapper {
	
	public Utility toEntity(UtilityRequest request, Detail detail) {
		return Utility.utilityBuilder()
				.hasAirConditioning(request.getHasAirConditioning())
				.hasDoorman(request.getHasDoorman())
				.hasElevator(request.getHasElevator())
				.detail(detail)
				.build();
	}
	
	public UtilitySpec toSpec(UtilityRequest request) {
		return UtilitySpec.builder()
				.hasAirConditioning(request.getHasAirConditioning())
				.hasDoorman(request.getHasDoorman())
				.hasElevator(request.getHasElevator())
				.build();
	}

	public UtilityResponse fromEntity(Utility utility) {
		return UtilityResponse.builder()
				.id(utility.getId())
				.createdDate(utility.getCreatedDate())
				.lastModifiedDate(utility.getLastModifiedDate())
				.hasAirConditioning(utility.getHasAirConditioning())
				.hasDoorman(utility.getHasDoorman())
				.hasElevator(utility.getHasElevator())
				.detailId(utility.getDetail().getId())
				.build();
	}
}
