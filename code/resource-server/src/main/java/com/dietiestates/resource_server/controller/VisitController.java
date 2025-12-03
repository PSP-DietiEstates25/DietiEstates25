package com.dietiestates.resource_server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.service.VisitService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/realestates/{realestateid}/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<VisitResponse> createVisit(
            @RequestBody VisitRequest request,
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt
    ){
        var userEmail = jwt.getSubject();

        var visit = visitService.createVisit(request, realestateid, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(visit);
    }

    @GetMapping("/{visitid}")
    public ResponseEntity<VisitResponse> getVisitById(
            @PathVariable Long realestateid,
            @PathVariable Long visitid
    ) {

        var dto = visitService.getVisitById(realestateid, visitid);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ESTATE_AGENT')")
    public ResponseEntity<Page<VisitResponse>> getRealEstateVisits(
            @PathVariable Long realestateid,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ) {
        var visits = visitService.getRealEstateVisits(realestateid, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(visits);
    }

    @PatchMapping("/{visitid}")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<VisitResponse> updateVisitStatus(
            @RequestBody VisitRequest request,
            @PathVariable Long realestateid,
            @PathVariable Long visitid,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var estateAgentEmail = jwt.getSubject();

        var visit = visitService.updateVisitStatus(request, realestateid, visitid);
        return ResponseEntity.status(HttpStatus.OK).body(visit);
    }
}

