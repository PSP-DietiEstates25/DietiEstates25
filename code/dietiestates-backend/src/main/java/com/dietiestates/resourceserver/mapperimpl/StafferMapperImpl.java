package com.dietiestates.resourceserver.mapperimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.response.StafferResponse;
import com.dietiestates.resourceserver.mapper.StafferMapper;
import com.dietiestates.resourceserver.model.Staffer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StafferMapperImpl implements StafferMapper {
	
	@Override
	public StafferResponse fromEntity(Staffer staffer) {
		return StafferResponse.builder()
				.id(staffer.getId())
				.email(staffer.getEmail())
				.adminEmail(staffer.getAdmin().getEmail())
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
