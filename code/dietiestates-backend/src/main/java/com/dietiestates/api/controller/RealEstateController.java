package com.dietiestates.api.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.service.RealEstateService;

import jakarta.validation.Valid;

import com.dietiestates.api.finder.RealEstateFinder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestates")
@RequiredArgsConstructor
public class RealEstateController {

	private final RealEstateService realEstateService;

	private final RealEstateFinder realEstateFinder;
	private static Logger logger = Logger.getLogger(RealEstateController.class.getName());

	@PostMapping
	public ResponseEntity<RealEstateResponse> createRealEstate(
			@RequestBody RealEstateRequest request) {
		var realEstate = realEstateService.createRealEstate(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(realEstate);
	}

	// GET /realestates/{id}
	@GetMapping("/{realestateid}")
	public ResponseEntity<RealEstateResponse> getRealEstateById(
			@PathVariable("realestateid") Long id) {
		var dto = realEstateService.getRealEstateById(id);
		return ResponseEntity.ok(dto);
	}

	// GET /realestates
	@GetMapping
	public ResponseEntity<List<RealEstateResponse>> listAllRealEstates() {
		var entities = realEstateFinder.getAllRealEstates();
		var dtos = realEstateService.createRealEstatesResponse(entities);
		return ResponseEntity.ok(dtos);
	}

	// PUT /realestates/{realestateid}
	@PutMapping("/{realestateid}")
	public ResponseEntity<RealEstateResponse> updateRealEstate(
			@PathVariable("realestateid") Long id,
			@RequestBody @Valid RealEstateRequest request) {

		var dto = realEstateService.updateRealEstate(id, request);
		return ResponseEntity.ok(dto);
	}

	// DELETE /realestates/{realestateid}
	@DeleteMapping("/{realestateid}")
	public ResponseEntity<Void> deleteRealEstate(
			@PathVariable("realestateid") Long id) {

		realEstateService.deleteRealEstate(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
