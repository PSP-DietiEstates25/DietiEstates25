package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.service.GeographicalPositionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/details/{detailid}/geographicalpositions")
@RequiredArgsConstructor
public class GeographicalPositionController {

	private final GeographicalPositionService geographicalPositionService;
	
	@PostMapping
	public ResponseEntity<?> createGeographicalPosition(
			@PathVariable Long detailid,
			@RequestBody GeographicalPositionRequest request
			){
		geographicalPositionService.createGeographicalPosition(request, detailid);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/{geographicalpositionid}")
	public ResponseEntity<GeographicalPositionResponse> getGeographicalPositionById(
			@PathVariable Long detailid, 
			@PathVariable Long geographicalpositionid
			){
		var geographicalPosition = geographicalPositionService.getGeographicalPositionById(detailid, geographicalpositionid);
		return ResponseEntity.status(HttpStatus.OK).body(geographicalPosition);
	}
}
