package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.AdminResponse;
import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;

import java.security.Principal;

public interface AdminService {

	AdminResponse register(StafferRequest request) throws RoleNotFoundException;

    AdminResponse getAdminById(Long adminId) throws AdminNotFoundException;
}
