package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.CreatedStaffersResponse;
import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AdminService {
	StafferResponse register(StafferRequest request, String creatorEmail) throws RoleNotFoundException;
    StafferResponse getAdminById(Long adminId) throws AdminNotFoundException;
    CreatedStaffersResponse getCreatedStaffers(String adminEmail, Integer page, Integer size);
}
