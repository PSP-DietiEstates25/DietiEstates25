package com.dietiestates.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.service.SearchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	
	@PostMapping
	public ResponseEntity<List<RealEstateDto>> createSearch(
			@RequestBody @Valid SearchDto request
			){
		System.out.println("Received SearchDto:" + request.toString());
		var realEstates = searchService.createSearch(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(realEstates);
	}
}
