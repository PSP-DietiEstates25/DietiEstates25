package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.dto.response.EstateAgentResponse;
import com.dietiestates.api.mapper.AccountMapper;
import com.dietiestates.api.mapper.EstateAgentMapper;
import com.dietiestates.api.mapper.RealEstateMapper;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.spec.StafferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentMapperImpl implements EstateAgentMapper {

	private final AccountMapper accountMapper;
	private final RealEstateMapper realEstateMapper;
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.stafferSpecBuilder()
				.email(request.getEmail())
				.password(request.getPassword())
				.accountLocked(false)
				.enabled(true)
				.adminEmail(request.getAdminEmail())
				.build();
	}
	
	@Override
	public EstateAgentResponse fromEntity(EstateAgent estateAgent) {
		return EstateAgentResponse.estateAgentResponseBuilder()
				.id(estateAgent.getId())
				.account(accountMapper.fromEntity(estateAgent.getSecurityAccountDecorator()))
				.adminEmail(estateAgent.getSecurityAccountDecorator().getAccountEmail())
				.createdRealEstates(realEstateMapper.createRealEsatatesResponse(estateAgent.getRealEstates()))
				.build();
	}
}
