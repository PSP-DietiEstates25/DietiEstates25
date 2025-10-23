package com.dietiestates.resourceserver.controller;

import javax.management.relation.RoleNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.dto.response.AuthenticationResponse;
import com.dietiestates.resourceserver.service.AdminAuthenticationService;
import com.dietiestates.resourceserver.service.AuthenticationService;

@RestController
@RequestMapping("/auth/admins")
public class AdminAuthenticationController extends AuthenticationController {

	private final AdminAuthenticationService adminAuthenticationService;
	
	public AdminAuthenticationController(
			AuthenticationService authenticationService,
			AdminAuthenticationService adminAuthenticationService
			) {
		super(authenticationService);
		this.adminAuthenticationService = adminAuthenticationService;
	}

	@PostMapping
	public ResponseEntity<AuthenticationResponse> registerAdmin(
			@RequestBody StafferRequest request
			) throws RoleNotFoundException{
		adminAuthenticationService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
