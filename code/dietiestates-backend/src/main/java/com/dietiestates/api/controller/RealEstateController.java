package com.dietiestates.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dietiestates.api.dto.CreateRealEstateAdRequest;
import com.dietiestates.api.dto.RealEstateAdResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.service.RealEstateAdQueryService;
import com.dietiestates.api.service.RealEstateAdService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/realestates" })
@RequiredArgsConstructor
@Validated
public class RealEstateController {

	private final RealEstateAdService adService;
	private final RealEstateAdQueryService queryService;

	// CREATE (AGENT/ADMIN) - multipart: payload JSON + photo file
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
	public RealEstateAdResponse createNewRealEstate(
			@RequestPart("payload") @Valid CreateRealEstateAdRequest payload,
			@RequestPart("photo") MultipartFile photo,
			Authentication authentication) throws Exception {

		String agentEmail = authentication.getName();
		return adService.create(payload, photo, agentEmail);
	}

	// Dashboard agente/admin: i miei annunci (paginati)
	@GetMapping("/dashboard")
	@PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
	public List<RealEstateAdResponse> mine(
			Authentication authentication,
			@RequestParam(required = false, defaultValue = "0") Integer page,
			@RequestParam(required = false, defaultValue = "12") Integer size) {

		String email = authentication.getName();
		return queryService.myAds(email, page, size);
	}

	// Ricerca pubblica (client): filtri opzionali + paging
	// Esempio:
	// GET
	// /api/ads/search?q=roma&category=SALE&minPrice=100000&maxPrice=300000&minRooms=3&energy=B&page=0&size=12
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
