package com.dietiestates.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.dietiestates.api.dto.request.CounterOfferRequest;
import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.dto.response.OfferResponse;
import com.dietiestates.api.mapper.OfferMapper;
import com.dietiestates.api.repository.OfferRepository;
import com.dietiestates.api.service.OfferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestates/{realestateid}/offers")
@RequiredArgsConstructor
@Validated
public class OfferController {

    private final OfferService offerService;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(
            @RequestBody OfferRequest request,
            @PathVariable Long realestateid) {
        var offer = offerService.createOffer(request, realestateid);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }

    // GET /realestates/{realestateid}/offers/{offerId}
    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOfferById(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable Long offerId) {
        var dto = offerService.getOfferById(realEstateId, offerId);
        return ResponseEntity.ok(dto);
    }

    // GET /realestates/{realestateid}/offers
    @GetMapping
    public ResponseEntity<List<OfferResponse>> listOffersForRealEstate(
            @PathVariable("realestateid") Long realEstateId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var entities = offerRepository.findByRealEstateId(realEstateId, pageable);
        var dtos = entities.stream().map(offerMapper::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("{id}/accept")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> accept(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable("id") Long id,
            Authentication auth) {
        return ResponseEntity.ok(offerService.acceptOffer(id, auth));
    }

    @PatchMapping("{id}/reject")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> reject(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable("id") Long id,
            Authentication auth) {
        return ResponseEntity.ok(offerService.rejectOffer(id, auth));
    }

    @PostMapping("{id}/counter")
    @PreAuthorize("hasAuthority('ESTATE_AGENT')")
    public ResponseEntity<OfferResponse> counter(
            @PathVariable("realestateid") Long realEstateId,
            @PathVariable("id") Long id,
            @Valid @RequestBody CounterOfferRequest body,
            Authentication auth) {
        return ResponseEntity.ok(offerService.counterOffer(id, body, auth));
    }
}
