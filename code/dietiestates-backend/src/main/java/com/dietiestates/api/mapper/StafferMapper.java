package com.dietiestates.api.mapper;

import java.util.List;

import com.dietiestates.api.dto.response.StafferResponse;
import com.dietiestates.api.model.Staffer;

public interface StafferMapper {

	StafferResponse fromEntity(Staffer staffer);
	
	List<StafferResponse> createStaffersResponse(List<Staffer> staffers);

}
