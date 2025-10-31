package com.dietiestates.api.controller;

import java.security.Principal;

import javax.management.relation.RoleNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.ChangePasswordRequest;
import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.service.AdminAuthenticationService;
import com.dietiestates.api.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/admins")
public class AdminAuthenticationController extends AuthenticationController {

	private final AdminAuthenticationService adminAuthenticationService;

	public AdminAuthenticationController(
			AuthenticationService authenticationService,
			AdminAuthenticationService adminAuthenticationService) {
		super(authenticationService);
		this.adminAuthenticationService = adminAuthenticationService;
	}

	@PostMapping
	public ResponseEntity<AuthenticationResponse> registerAdmin(
			@RequestBody StafferRequest request) throws RoleNotFoundException {
		adminAuthenticationService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/password")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> changePassword(
			@RequestBody @Valid ChangePasswordRequest req,
			Principal principal) {
		if (principal == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		adminAuthenticationService.changeOwnPassword(principal.getName(), req);
		return ResponseEntity.noContent().build();
	}
}
