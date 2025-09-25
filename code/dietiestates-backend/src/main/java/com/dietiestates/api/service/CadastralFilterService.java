package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.CadastralFilterDto;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.repository.CadastralFilterRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralFilterService {

	private final CadastralFilterRepository cadastralFilterRepository;
	private final SearchRepository searchRepository;
	
	public void createCadastralFilter(CadastralFilterDto request, Long searchId) {
		var cadastralFilter = of(request, searchId);
		cadastralFilterRepository.save(cadastralFilter);
	}
	
	private CadastralFilter of(CadastralFilterDto request, Long searchId) {
		
		var search = searchRepository.findById(searchId)
				.orElseThrow(SearchNotFoundException::new);
				
		return CadastralFilter.cadastralFilterBuilder()
				.createdDate(LocalDateTime.now())
				.minPrice(request.getMinPrice())
				.maxPrice(request.getMaxPrice())
				.minSquareMeters(request.getMinSquareMeters())
				.maxSquareMeters(request.getMaxSquareMeters())
				.minEnergyClass(request.getMinEnergyClass())
				.maxEnergyClass(request.getMaxEnergyClass())
				.minRooms(request.getMinRooms())
				.maxRooms(request.getMaxRooms())
				.minFloor(request.getMinFloor())
				.maxFloor(request.getMaxFloor())
				.search(search)
				.build();
	}
}
