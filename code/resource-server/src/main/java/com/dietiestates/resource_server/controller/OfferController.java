package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
@RequestMapping("/offers")
@RequiredArgsConstructor
@Validated
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ESTATE_AGENT', 'OIDC_USER')")
    public ResponseEntity<OfferResponse> createOffer(
            @RequestBody OfferRequest request,
            @RequestParam Long realestateid,
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication
    ){

        var creatorEmail = jwt.getSubject();
        var creatorRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(
                        authority ->
                                    authority.equals(Role.USER.name()) ||
                                    authority.equals(Role.OIDC_USER.name()) ||
                                    authority.equals(Role.ESTATE_AGENT.name())
                )
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Missing required role"));

        var offer = offerService.createOffer(request, realestateid, creatorEmail, creatorRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }

    @GetMapping("/{offerid}")
    public ResponseEntity<OfferResponse> getOfferById(
            @RequestParam Long realestateid,
            @PathVariable Long offerid
    ) {
        var offer = offerService.getOfferById(realestateid, offerid);
        return ResponseEntity.status(HttpStatus.OK).body(offer);
    }

    //può ritornare a seconda della presenza di ?realestateid le offerte paginate del realestate o dell'utente o dell'agente
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ESTATE_AGENT', 'OIDC_USER')")
    public ResponseEntity<Page<OfferResponse>> getOffers(
            @RequestParam(required = false) Long realestateid,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication
    ) {

        var creatorEmail = jwt.getSubject();
        var creatorRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority ->
                                    authority.equals(Role.USER.name()) ||
                                    authority.equals(Role.ESTATE_AGENT.name()) ||
                                    authority.equals(Role.OIDC_USER.name())
                )
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Missing required role"));

        if(realestateid != null){
            var offers = offerService.getRealEstateOffers(realestateid, creatorEmail, creatorRole, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(offers);
        }

        var offers = offerService.getOffers(creatorEmail, creatorRole, status, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

    @PatchMapping("/{offerid}")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> updateOfferStatus(
            @RequestBody OfferRequest request,
            @RequestParam Long realestateid,
            @PathVariable Long offerid
    ) {
        var offer = offerService.updateOfferStatus(request, realestateid, offerid);
        return ResponseEntity.status(HttpStatus.OK).body(offer);
    }
}

