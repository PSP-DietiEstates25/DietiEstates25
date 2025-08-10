package com.dietiestates.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.api.dto.CreateAgentRequest;
import com.dietiestates.api.service.EstateAgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final EstateAgentService estateAgentService;

    public AdminController(EstateAgentService estateAgentService) {
        this.estateAgentService = estateAgentService;
    }

    @PostMapping("/agents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createAgent(@RequestBody CreateAgentRequest req) {
        estateAgentService.createAgent(req.email(), req.password());
        return ResponseEntity.ok().build();
    }
    
}
