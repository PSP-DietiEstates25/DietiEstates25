package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.dto.response.AdminResponse;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.spec.StafferSpec;

public interface AdminMapper {

	StafferSpec toSpec(StafferRequest request);
	
	AdminResponse fromEntity(Admin admin);
}
