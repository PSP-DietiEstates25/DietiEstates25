package com.dietiestates.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.api.dto.RealEstateAdResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.service.RealEstateAdQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class RealEstateAdQueryController {

    private final RealEstateAdQueryService queryService;

    /**
     * dashboard dell'agente: i miei annunci
     * richiede ruolo AGENT o ADMIN. usa l'email dal JWT
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public List<RealEstateAdResponse> mine(Authentication authentication) {
        String agentEmail = authentication.getName();
        return queryService.listMine(agentEmail);
    }

    /**
     * ricerca generale: filtri opzionali + paging
     * ssempio:
     * GET
     * /api/ads/search?q=roma&category=SALE&minPrice=100000&maxPrice=300000&minRooms=3&energy=B&page=0&size=12
     */
    @GetMapping("/search")
    public List<RealEstateAdResponse> search(
            @RequestParam(required = false) AdCategory category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) EnergyClass energy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer size) {
        return queryService.search(category, q, minPrice, maxPrice, minRooms, energy, page, size);
    }
}