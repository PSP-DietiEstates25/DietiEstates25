package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.service.UtilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/details/{detailid}/utilities")
@RequiredArgsConstructor
public class UtilityController {

	private final UtilityService utilityService;
	
	@PostMapping
	public ResponseEntity<?> createUtility(
			@PathVariable Long detailid,
			@RequestBody UtilityRequest request
			){
		utilityService.createUtility(request, detailid);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/{utilityid}")
	public ResponseEntity<UtilityResponse> getUtilityById(
			@PathVariable Long detailid,
			@PathVariable Long utilityid
			){
		var utility = utilityService.getUtilityById(detailid, utilityid);
		return ResponseEntity.status(HttpStatus.OK).body(utility);
	}
}
