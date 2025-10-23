package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.dto.response.EstateAgentResponse;
import com.dietiestates.resourceserver.mapper.EstateAgentMapper;
import com.dietiestates.resourceserver.mapper.RealEstateMapper;
import com.dietiestates.resourceserver.model.EstateAgent;
import com.dietiestates.resourceserver.spec.StafferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentMapperImpl implements EstateAgentMapper {

	private final RealEstateMapper realEstateMapper;
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.stafferSpecBuilder()
				.email(request.getEmail())
				.adminEmail(request.getAdminEmail())
				.build();
	}
	
	@Override
	public EstateAgentResponse fromEntity(EstateAgent estateAgent) {
		return EstateAgentResponse.estateAgentResponseBuilder()
				.id(estateAgent.getId())
				.email(estateAgent.getEmail())
				.adminEmail(estateAgent.getAdmin().getEmail())
				.createdRealEstates(realEstateMapper.createRealEsatatesResponse(estateAgent.getRealEstates()))
				.build();
	}
}
