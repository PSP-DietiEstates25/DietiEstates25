package com.dietiestates.resource_server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.service.OfferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestates/{realestateid}/offers")
@RequiredArgsConstructor
@Validated
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER, ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> createOffer(
            @RequestBody OfferRequest request,
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication
    ){

        var creatorEmail = jwt.getSubject();
        var creatorRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.equals("USER") || authority.equals("ESTATE_AGENT"))
                .findFirst().toString();

        var offer = offerService.createOffer(request, realestateid, creatorEmail, creatorRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }

    @GetMapping("/{offerid}")
    public ResponseEntity<OfferResponse> getOfferById(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable Long offerid
    ) {
        var offer = offerService.getOfferById(realEstateId, offerid);
        return ResponseEntity.status(HttpStatus.OK).body(offer);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER, ESTATE_AGENT')")
    public ResponseEntity<Page<OfferResponse>> getRealEstateOffers(
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ) {

        var creatorEmail = jwt.getSubject();
        var creatorRole = jwt.getClaim("role").toString();

        var offers = offerService.getRealEstateOffers(realestateid, creatorEmail, creatorRole, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

    @PatchMapping("/{offerid}")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> updateOfferStatus(
            @RequestBody OfferRequest request,
            @PathVariable Long realestateid,
            @PathVariable Long offerid
    ) {
        var offer = offerService.updateOfferStatus(request, realestateid, offerid);
        return ResponseEntity.status(HttpStatus.OK).body(offer);
    }
}

