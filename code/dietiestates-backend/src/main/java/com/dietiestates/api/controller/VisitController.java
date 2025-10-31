package com.dietiestates.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.mapper.VisitMapper;
import com.dietiestates.api.repository.VisitRepository;
import com.dietiestates.api.service.VisitService;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestates/{realestateid}/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(
            @RequestBody VisitRequest request,
            @PathVariable Long realestateid) {
        var visit = visitService.createVisit(request, realestateid);
        return ResponseEntity.status(HttpStatus.OK).body(visit);
    }

    // GET /realestates/{realestateid}/visits/{visitId}
    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponse> getVisitById(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable Long visitId) {
        var dto = visitService.getVisitById(realEstateId, visitId);
        return ResponseEntity.ok(dto);
    }

    // GET /realestates/{realestateid}/visits
    @GetMapping
    public ResponseEntity<List<VisitResponse>> listVisitsForRealEstate(
            @PathVariable("realestateid") Long realEstateId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var entities = visitRepository.findByRealEstateId(realEstateId, pageable);
        var dtos = entities.stream().map(visitMapper::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    @Operation(summary = "Accept a visit request")
    public ResponseEntity<VisitResponse> accept(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(visitService.acceptVisit(id, auth));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    @Operation(summary = "Reject a visit request")
    public ResponseEntity<VisitResponse> reject(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(visitService.rejectVisit(id, auth));
    }
}
