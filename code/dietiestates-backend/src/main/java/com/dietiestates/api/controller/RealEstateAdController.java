package com.dietiestates.api.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dietiestates.api.dto.CreateRealEstateAdRequest;
import com.dietiestates.api.dto.RealEstateAdResponse;
import com.dietiestates.api.service.RealEstateAdService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestates")
@RequiredArgsConstructor // genera il costruttore con i campi final per l'injection
@Validated
public class RealEstateAdController {

	
    private final RealEstateAdService adService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
    public RealEstateAdResponse createAd(
            // "payload" è il nome della parte multipart che contiene il JSON
            @RequestPart("payload") @Valid CreateRealEstateAdRequest payload,
            // "photo" è la parte multipart che contiene il file immagine
            @RequestPart("photo") MultipartFile photo,

            Authentication authentication) throws Exception {

        String agentEmail = authentication.getName();

        // deleghiamo al service: valida relazioni (Detail, Agent), converte DTO ->
        // Entity, salva e ritorna DTO di risposta
        return adService.create(payload, photo, agentEmail);
    }
    
    @GetMapping("/realestates/{realestateid}")
    public RealEstateDTO
}
