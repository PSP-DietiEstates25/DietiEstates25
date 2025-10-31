package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.EstateAgentResponse;
import com.dietiestates.resource_server.mapper.EstateAgentMapper;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.spec.StafferSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EstateAgentMapperDefaultImpl implements EstateAgentMapper {

	private final RealEstateMapper realEstateMapper;
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.builder()
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
				.createdRealEstates(realEstateMapper.createRealEstatesResponse(estateAgent.getRealEstates()))
				.build();
	}
}
