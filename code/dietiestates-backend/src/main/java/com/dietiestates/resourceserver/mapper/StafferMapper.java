package com.dietiestates.resourceserver.mapper;

import java.util.List;

import com.dietiestates.resourceserver.dto.response.StafferResponse;
import com.dietiestates.resourceserver.model.Staffer;

public interface StafferMapper {

	StafferResponse fromEntity(Staffer staffer);
	
	List<StafferResponse> createStaffersResponse(List<Staffer> staffers);

}
