package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.service.DetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
public class DetailController {

	private final DetailService detailService;

	@PostMapping
	public ResponseEntity<?> createDetails(
			@RequestBody DetailRequest request
				) {
		detailService.createDetail(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
