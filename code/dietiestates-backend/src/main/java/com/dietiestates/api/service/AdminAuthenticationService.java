package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.ChangePasswordRequest;
import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;

public interface AdminAuthenticationService extends AuthenticationService {

	void register(StafferRequest request) throws RoleNotFoundException;

	void changeOwnPassword(String email, ChangePasswordRequest req);

}
