package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.service.EstateAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;

@RestController
@RequestMapping("/api/estateagents")
@RequiredArgsConstructor
public class EstateAgentController {

    private final EstateAgentService estateAgentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StafferResponse> registerEstateAgent(
            @RequestBody StafferRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) throws RoleNotFoundException {

        var creatorEmail = jwt.getSubject();

        var estateAgent = estateAgentService.register(request, creatorEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(estateAgent);
    }

    @GetMapping("/{estateagentid}")
    public ResponseEntity<StafferResponse> getEstateAgentById(
            @PathVariable Long estateagentid
    ) throws EstateAgentNotFoundException {

        var estateAgent = estateAgentService.getEstateAgentById(estateagentid);
        return ResponseEntity.status(HttpStatus.CREATED).body(estateAgent);
    }
}