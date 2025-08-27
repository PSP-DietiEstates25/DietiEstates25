package com.dietiestates.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.RealEstateDto;

import lombok.RequiredArgsConstructor;

@RestController("/realestates")
@RequiredArgsConstructor
public class RealEstateController {

	private final RealEstateService realEstateService;
	
	@PostMapping()
	public RealEstateDto createNewRealEstate() {
		
	}
	
	@GetMapping("/{realestateid}")
	public RealEstateDto getRealEstate(
			@PathVariable String realestateid
			) {
		
	}
	
}
