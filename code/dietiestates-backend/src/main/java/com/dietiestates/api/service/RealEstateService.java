package com.dietiestates.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;

	public Optional<RealEstate> getRealEstateById(Long id) {
		return realEstateRepository.findById(id);
	}
}
