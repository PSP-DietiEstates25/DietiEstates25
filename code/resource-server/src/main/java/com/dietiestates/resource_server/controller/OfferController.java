package com.dietiestates.resource_server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<OfferResponse> createUserOffer(
            @RequestBody OfferRequest request,
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt
    ){

        var userEmail = jwt.getSubject();
        var offer = offerService.createUserOffer(request, realestateid, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ESTATE_AGENT'')")
    public ResponseEntity<OfferResponse> createEstatAgentCounterOffer(
            @RequestBody OfferRequest request,
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt
    ){

        var estateAgentEmail = jwt.getSubject();
        var offer = offerService.createEstateAgentCounterOffer(request, realestateid, estateAgentEmail);
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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Page<OfferResponse>> getUserOffers(
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ) {

        var userEmail = jwt.getSubject();
        var offers = offerService.getUserOffers(realestateid, userEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<Page<OfferResponse>> getEstateAgentOffers(
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ){
        var estateAgentEmail = jwt.getSubject();
        var offers = offerService.getEstateAgentOffers(realestateid, estateAgentEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

    @GetMapping
    public ResponseEntity<Page<OfferResponse>> getRealEstateOffers(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @PathVariable Long realestateid
    ){
        var offers = offerService.getRealEstateOffers(realestateid, page, size);
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

