package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.DetailsDto;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.service.DetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
public class DetailsController {

	private final DetailsService detailsService;

	@PostMapping
	public ResponseEntity<Detail> createDetails(
			@RequestBody DetailsDto request
			) {
		return ResponseEntity.status(HttpStatus.CREATED).body(detailsService.createDetails(request));
	}
}
