package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.dto.response.AdminResponse;
import com.dietiestates.resourceserver.model.Admin;
import com.dietiestates.resourceserver.spec.StafferSpec;

public interface AdminMapper {

	StafferSpec toSpec(StafferRequest request);
	
	AdminResponse fromEntity(Admin admin);
}
