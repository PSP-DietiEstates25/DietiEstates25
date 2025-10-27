package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.dto.response.EstateAgentResponse;
import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.service.EstateAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;

@RestController
@RequestMapping("/estateagents")
@RequiredArgsConstructor
public class EstateAgentController {

    private final EstateAgentService estateAgentService;

    @PostMapping
    public ResponseEntity<EstateAgentResponse> registerEstateAgent(
            @RequestBody StafferRequest request
    ) throws RoleNotFoundException {

        var estateAgent = estateAgentService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(estateAgent);

    }

    @GetMapping("/{estataeagentid}")
    public ResponseEntity<EstateAgentResponse> getEstateAgentById(
            @PathVariable Long estateagentid
    ) throws EstateAgentNotFoundException {

        var estateAgent = estateAgentService.getEstateAgentById(estateagentid);
        return ResponseEntity.status(HttpStatus.CREATED).body(estateAgent);
    }
}
