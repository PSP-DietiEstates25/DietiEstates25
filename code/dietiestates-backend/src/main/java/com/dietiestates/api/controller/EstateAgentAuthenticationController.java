package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.service.AuthenticationService;
import com.dietiestates.api.service.EstateAgentAuthenticationService;

@RestController
@RequestMapping("/auth/estateagents")
public class EstateAgentAuthenticationController extends AuthenticationController {

	private final EstateAgentAuthenticationService estateAgentAuthenticationService;
	
	public EstateAgentAuthenticationController(
			AuthenticationService authenticationService,
			EstateAgentAuthenticationService estateAgentAuthenticationService
			) {
		super(authenticationService);
		this.estateAgentAuthenticationService = estateAgentAuthenticationService;
	}
	
	@PostMapping
	public ResponseEntity<EstateAgent> registerEstateAgent(
			@RequestBody StafferRequest request
			) throws RoleNotFoundException {
		estateAgentAuthenticationService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
