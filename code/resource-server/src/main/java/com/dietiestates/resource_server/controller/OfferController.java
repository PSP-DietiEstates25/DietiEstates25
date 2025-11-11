package com.dietiestates.resource_server.controller;

import jakarta.validation.Valid;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.service.OfferService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Page<OfferResponse>> getPagedUserRealEstateOffers(
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size
    ) {

        var userEmail = jwt.getSubject();

        var offers = offerService.getPagedUserRealEstateOffers(realestateid, userEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<Page<OfferResponse>> getPagedEstateAgentCounterOffers(
            @PathVariable Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size
    ){
        var estateAgentEmail = jwt.getSubject();

        var offers = offerService.getPagedEstateAgentRealEstateOffers(realestateid, estateAgentEmail, page, size);
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

    /* La logica della creazione di una contro offerta è spostata nella logica di creazione di un offerta, in base alla authority presa
    @PostMapping("{id}/counter")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> counter(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable("id") Long id,
            @Valid @RequestBody CounterOfferRequest body,
            Authentication auth) {
        return ResponseEntity.ok(offerService.counterOffer(id, body, auth));
    }
    */
}

