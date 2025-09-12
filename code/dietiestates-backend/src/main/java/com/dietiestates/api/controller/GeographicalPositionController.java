package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.GeographicalPositionDto;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.service.GeographicalPositionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/geographicals")
@RequiredArgsConstructor
public class GeographicalPositionController {

	private final GeographicalPositionService geographicalPositionService;
	
	@PostMapping
	public ResponseEntity<GeographicalPosition> createGeographicalPosition(
			@RequestBody GeographicalPositionDto request
			){
		return ResponseEntity.status(HttpStatus.CREATED).body(geographicalPositionService.createGeographicalPosition(request));
	}
}
