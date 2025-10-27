package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.mapper.StafferMapper;
import com.dietiestates.resource_server.model.Staffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StafferMapperDefaultImpl implements StafferMapper {
	
	@Override
	public StafferResponse fromEntity(Staffer staffer) {
		return StafferResponse.stafferResponseBuilder()
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
