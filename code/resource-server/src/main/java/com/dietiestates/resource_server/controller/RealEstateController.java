package com.dietiestates.resource_server.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.service.RealEstateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestates")
@RequiredArgsConstructor
public class RealEstateController {

    private final RealEstateService realEstateSerivce;

    @PostMapping
    public ResponseEntity<RealEstateResponse> createRealEstate(
            @RequestBody RealEstateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        var estateAgentEmail = jwt.getSubject();

        var realEstate = realEstateSerivce.createRealEstate(request, estateAgentEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(realEstate);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<Page<RealEstateResponse>> getEstateAgentRealEstates(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @AuthenticationPrincipal Jwt jwt
    ){
        var estateAgentEmail = jwt.getSubject();

        var realEstates = realEstateSerivce.getEstateAgentRealEstates(estateAgentEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(realEstates);
    }
    /*
    @GetMapping
    public ResponseEntity<List<RealEstateResponse>> getAllRealEstates() {
        var all = realEstateFinder.getAllRealEstates();
        var response = realEstateSerivce.createRealEstatesResponse(all);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    */

    @GetMapping
    public ResponseEntity<Page<RealEstateResponse>> getSearchRealEstates(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam Long searchid
    ){
        var realEstates = realEstateSerivce.getSearchRealEstates(searchid, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(realEstates);
    }

    @GetMapping("/{realestateid}")
    public ResponseEntity<RealEstateResponse> getRealEstateById(
            @PathVariable Long realestateid
    ) {

        var realEstate = realEstateSerivce.getRealEstateById(realestateid);
        return ResponseEntity.status(HttpStatus.OK).body(realEstate);
    }

    @GetMapping
    public ResponseEntity<Page<RealEstateResponse>> getPagedRealEstates(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size
    ) {

        var pagedRealEstates = realEstateSerivce.getPagedRealEstates(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(pagedRealEstates);
    }

    @PutMapping("/{realestateid}")
    public ResponseEntity<RealEstateResponse> updateRealEstate(
            @PathVariable Long realestateid,
            @RequestBody @Valid RealEstateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        var estateAgentEmail = jwt.getSubject();

        var realEstate = realEstateSerivce.updateRealEstate(realestateid, request, estateAgentEmail);
        return ResponseEntity.status(HttpStatus.OK).body(realEstate);
    }

    @DeleteMapping("/{realestateid}")
    public ResponseEntity<Void> deleteRealEstate(
            @PathVariable Long realestateid
    ) {

        realEstateSerivce.deleteRealEstate(realestateid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
