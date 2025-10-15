package com.authorizationserver.api.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.core.Authentication;

import com.authorizationserver.api.dto.request.AccountRequest;
import com.authorizationserver.api.dto.response.AccountResponse;

public interface AuthenticationService {

	AccountResponse register(AccountRequest request) throws RoleNotFoundException;

	String login(Authentication authentication);
}
