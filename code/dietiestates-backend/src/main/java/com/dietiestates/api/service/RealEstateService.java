package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.repository.RealEstateAdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {

	private final RealEstateAdRepository realEstateRepository;
	
	
}
