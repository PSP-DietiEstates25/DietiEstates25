package com.dietiestates.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.DetailsDto;
import com.dietiestates.api.model.Details;
import com.dietiestates.api.service.DetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
public class DetailsController {

	private final DetailsService detailsService;

	public Details createDetails(DetailsDto request) {
		var details = Details.builder().build();
		return details;
	}
}
