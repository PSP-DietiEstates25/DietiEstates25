package com.dietiestates.resourceserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;
import com.dietiestates.resourceserver.model.EstateAgent;
import com.dietiestates.resourceserver.service.AuthenticationService;
import com.dietiestates.resourceserver.service.EstateAgentAuthenticationService;

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
