package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.service.AdminAuthenticationService;
import com.dietiestates.api.service.AuthenticationService;

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
			){
		adminAuthenticationService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
