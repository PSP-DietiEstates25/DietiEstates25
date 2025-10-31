package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.AdminResponse;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.spec.StafferSpec;

public interface AdminMapper {

	StafferSpec toSpec(StafferRequest request);
	
	AdminResponse fromEntity(Admin admin);
}
