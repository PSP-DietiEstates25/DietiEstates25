package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.CadastralFilterDto;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.service.CadastralFilterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/searches/{searchid}/cadastral-filters")
@RequiredArgsConstructor
public class CadastralFilterController {

	private final CadastralFilterService cadastralFilterService;
	
	@PostMapping
	public ResponseEntity<CadastralFilter> createCadastralFilter(
			@PathVariable Long searchid,
			@RequestBody CadastralFilterDto request){
		cadastralFilterService.createCadastralFilter(request, searchid);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
