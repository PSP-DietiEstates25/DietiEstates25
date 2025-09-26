package com.dietiestates.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	
	@PostMapping
	public ResponseEntity<List<RealEstate>> createSearch(
			@RequestBody SearchDto request
			){
		var realEstates = searchService.createSearch(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(realEstates);
	}
}
