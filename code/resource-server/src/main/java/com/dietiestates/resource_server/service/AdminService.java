package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;

public interface AdminService {
	StafferResponse register(StafferRequest request, String creatorEmail) throws RoleNotFoundException;
    StafferResponse getAdminById(Long adminId) throws AdminNotFoundException;
}
