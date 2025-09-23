package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	
	@PostMapping
	public ResponseEntity<Search> createSearch(
			@RequestBody SearchDto request
			){
		searchService.createSearch(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
