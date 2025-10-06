package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;

public interface EstateAgentAuthenticationService extends AuthenticationService {

	void register(StafferRequest request) throws RoleNotFoundException;
}
