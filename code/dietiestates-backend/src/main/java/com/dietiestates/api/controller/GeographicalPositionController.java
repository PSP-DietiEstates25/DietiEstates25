package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.service.GeographicalPositionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/geographicalpositions")
@RequiredArgsConstructor
public class GeographicalPositionController {

	private final GeographicalPositionService geographicalPositionService;

	@PostMapping
	public ResponseEntity<GeographicalPositionResponse> createGeographicalPosition(
			@RequestBody GeographicalPositionRequest request) {
		var geographicalPosition = geographicalPositionService.createGeographicalPosition(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(geographicalPosition);
	}

	@GetMapping("/{geographicalpositionid}")
	public ResponseEntity<GeographicalPositionResponse> getGeographicalPositionById(
			@PathVariable Long geographicalpositionid) {
		var geographicalPosition = geographicalPositionService.getGeographicalPositionById(geographicalpositionid);
		return ResponseEntity.status(HttpStatus.OK).body(geographicalPosition);
	}

	@PutMapping("/{geographicalpositionid}")
	public ResponseEntity<GeographicalPositionResponse> updateGeographicalPosition(
			@PathVariable Long geographicalpositionid,
			@RequestBody GeographicalPositionRequest request) {
		var dto = geographicalPositionService.updateGeographicalPosition(geographicalpositionid, request);
		return ResponseEntity.ok(dto);
	}
}
