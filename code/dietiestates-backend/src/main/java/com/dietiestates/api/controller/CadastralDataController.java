package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.service.CadastralDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cadastraldata")
@RequiredArgsConstructor
public class CadastralDataController {

	private final CadastralDataService cadastralDataService;
	
	@PostMapping
	public ResponseEntity<?> createCadastralData(
			@RequestBody CadastralDataRequest request
				){
		cadastralDataService.createCadastralData(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/{cadastraldataid}")
	public ResponseEntity<CadastralDataResponse> getCadastralDataById(
			@PathVariable Long cadastraldataid
			){
		var cadastralData = cadastralDataService.getCadastralDataById(cadastraldataid);
		return ResponseEntity.status(HttpStatus.OK).body(cadastralData);
	}
}
