package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;

public interface EstateAgentAuthenticationService extends AuthenticationService {

	void register(StafferRequest request) throws RoleNotFoundException;
}
