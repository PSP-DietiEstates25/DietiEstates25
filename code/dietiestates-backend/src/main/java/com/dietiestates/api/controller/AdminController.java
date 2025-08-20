package com.dietiestates.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.CreateAgentRequest;
import com.dietiestates.api.service.EstateAgentService;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	
	@Autowired
    private EstateAgentService estateAgentService;

    @PostMapping("/agents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createAgent(@RequestBody CreateAgentRequest req) {
        estateAgentService.createAgent(req.email(), req.password());
        return ResponseEntity.ok().build();
    }
    
    
}
