package com.dietiestates.resourceserver.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.resourceserver.dto.request.SearchRequest;
import com.dietiestates.resourceserver.dto.response.RealEstateResponse;
import com.dietiestates.resourceserver.service.SearchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	
	@PostMapping()
	public ResponseEntity<List<RealEstateResponse>> createSearch(
			@RequestBody @Valid SearchRequest request
			){
		var realEstates = searchService.createSearch(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(realEstates);
	}
}
