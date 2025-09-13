package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.CadastralDataDto;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.service.CadastralDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cadastrals")
@RequiredArgsConstructor
public class CadastralDataController {

	private final CadastralDataService cadastralDataService;
	
	@PostMapping
	public ResponseEntity<CadastralData> createCadastralData(
			@RequestBody CadastralDataDto request
			){
		cadastralDataService.createCadastralData(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
