package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.UtilityDto;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.service.UtilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/utilities")
@RequiredArgsConstructor
public class UtilityController {

	private final UtilityService utilityService;
	
	@PostMapping
	public ResponseEntity<Utility> createUtility(
			@RequestBody UtilityDto request
			){
		return ResponseEntity.status(HttpStatus.CREATED).body(utilityService.createUtility(request));
	}
}
