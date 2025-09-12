package com.dietiestates.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.model.Offer;
import com.dietiestates.api.service.OfferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/realestate/{realestateId}/offers")
@RequiredArgsConstructor
@Validated
public class OfferController {

	/*
    private final OfferService offerService;

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT') and !hasAnyAuthority('AGENT','ADMIN')")
    public ResponseEntity<Offer> propose(
            @PathVariable Long realestateId,
            @Valid @RequestBody OfferProposalRequest payload,
            Authentication authentication) {

        String email = authentication.getName();
        Offer offer = offerService.propose(email, realestateId, payload);

        // in uscita ritorna direttamente l'ENTITY, con ResponseEntity parametrici
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(offer);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
    public ResponseEntity<List<Offer>> listForEstate(
            @PathVariable Long adId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size,
            Authentication authentication) {

        String email = authentication.getName();
        List<Offer> offers = offerService.listForEstate(email, adId, page, size);
        return ResponseEntity.ok(offers);
    }
    */
}
