package com.dietiestates.api.mapperimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.response.StafferResponse;
import com.dietiestates.api.mapper.AccountMapper;
import com.dietiestates.api.mapper.StafferMapper;
import com.dietiestates.api.model.Staffer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StafferMapperImpl implements StafferMapper {

	private final AccountMapper accountMapper;
	
	@Override
	public StafferResponse fromEntity(Staffer staffer) {
		return StafferResponse.builder()
				.id(staffer.getId())
				.account(accountMapper.fromEntity(staffer.getSecurityAccountDecorator()))
				.adminEmail(staffer.getAdmin().getSecurityAccountDecorator().getAccountEmail())
				.build();
	}
	
	@Override
	public List<StafferResponse> createStaffersResponse(List<Staffer> staffers) {
		
		var response = new ArrayList<StafferResponse>();
		staffers.forEach(staffer -> {
			var stafferResponse = fromEntity(staffer);
			response.add(stafferResponse);
		});
		
		return response;
	}

}
