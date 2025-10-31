package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.model.Staffer;

import java.util.List;

public interface StafferMapper {

	StafferResponse fromEntity(Staffer staffer);
	
	List<StafferResponse> createStaffersResponse(List<Staffer> staffers);

}
