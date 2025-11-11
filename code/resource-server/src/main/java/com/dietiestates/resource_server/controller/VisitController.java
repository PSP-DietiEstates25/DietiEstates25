package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.model.Visit;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.service.VisitService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/realestates/{realestateid}/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Page<VisitResponse>> getUserVisits(
            @PathVariable Long realestateid,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var userEmail = jwt.getSubject();

        var visits = visitService.getUserVisits(realestateid, userEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(visits);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<Page<VisitResponse>> getEstateAgentVisits(
            @PathVariable Long realestateid,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var estateAgentEmail = jwt.getSubject();

        var visits = visitService.getEstateAgentVisits(realestateid, estateAgentEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(visits);
    }

    @GetMapping
    public ResponseEntity<Page<VisitResponse>> getRealEstateVisits(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @PathVariable Long realestateid
    ){
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
    /*
    @GetMapping
    public ResponseEntity<List<VisitResponse>> listVisitsForRealEstate(
            @PathVariable Long realestateid,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var entities = visitRepository.findByRealEstateId(realEstateId, pageable);
        var dtos = entities.stream().map(visitMapper::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{visitid}")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<VisitResponse> updateVisitStatus(
            @PathVariable Long visitid,
            Authentication auth
    ) {

        return ResponseEntity.ok(visitService.acceptVisit(id, auth));
    }
    */
}

