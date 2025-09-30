package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.CadastralFilterDto;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.CadastralFilterRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralFilterService {

	private final CadastralFilterRepository cadastralFilterRepository;
	private final SearchRepository searchRepository;
	
	public CadastralFilter createCadastralFilter(CadastralFilterDto request, Long searchId) {
		var cadastralFilter = of(request, searchId);
		return cadastralFilterRepository.save(cadastralFilter);
	}
	
	public CadastralFilter of(CadastralFilterDto request, Long searchId) {
		
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
	
	public List<RealEstate> getCadastralFilterRealEstates(CadastralFilter searchCadastralFilter,  List<RealEstate> realEstates){
		var cadastralFilterRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateCadastralData = realEstate.getCadastralData();
			if(
					(
						(
							searchCadastralFilter.getPriceRange().getMinPrice().compareTo(realEstateCadastralData.getPrice()) == -1 ||
							searchCadastralFilter.getPriceRange().getMinPrice().compareTo(realEstateCadastralData.getPrice()) == 0
						)
						&&
						(
							realEstateCadastralData.getPrice().compareTo(searchCadastralFilter.getPriceRange().getMaxPrice()) == -1 ||
							realEstateCadastralData.getPrice().compareTo(searchCadastralFilter.getPriceRange().getMaxPrice()) == 0
						)
					)
					&&
					(
						(
							searchCadastralFilter.getSquareMetersRange().getMinSquareMeters().compareTo(realEstateCadastralData.getSquareMeters()) == -1 ||
							searchCadastralFilter.getSquareMetersRange().getMinSquareMeters().compareTo(realEstateCadastralData.getSquareMeters()) == 0
						)
						&&
						(
							realEstateCadastralData.getSquareMeters().compareTo(searchCadastralFilter.getSquareMetersRange().getMaxSquareMeters()) == -1 ||
							realEstateCadastralData.getSquareMeters().compareTo(searchCadastralFilter.getSquareMetersRange().getMaxSquareMeters()) == 0
						)
					)
					&&
					(
						(
							searchCadastralFilter.getEnergyClassRange().getMinEnergyClass().compareTo(realEstateCadastralData.getEnergyClass().getOrder()) == -1 ||
							searchCadastralFilter.getEnergyClassRange().getMinEnergyClass().compareTo(realEstateCadastralData.getEnergyClass().getOrder()) == 0
						)
						&&
						(
							realEstateCadastralData.getEnergyClass().getOrder().compareTo(searchCadastralFilter.getEnergyClassRange().getMaxEnergyClass()) == -1 ||
							realEstateCadastralData.getEnergyClass().getOrder().compareTo(searchCadastralFilter.getEnergyClassRange().getMaxEnergyClass()) == 0
						)
					)
					&&
					(
						(
							searchCadastralFilter.getRoomsRange().getMinRooms().compareTo(realEstateCadastralData.getRooms()) == -1 ||
							searchCadastralFilter.getRoomsRange().getMinRooms().compareTo(realEstateCadastralData.getRooms()) == 0
						)
						&&
						(
							realEstateCadastralData.getRooms().compareTo(searchCadastralFilter.getRoomsRange().getMaxRooms()) == -1 ||
							realEstateCadastralData.getRooms().compareTo(searchCadastralFilter.getRoomsRange().getMaxRooms()) == 0
						)
					)
					&&
					(
						(
							searchCadastralFilter.getFloorRange().getMinFloor().compareTo(realEstateCadastralData.getFloor()) == -1 ||
							searchCadastralFilter.getFloorRange().getMinFloor().compareTo(realEstateCadastralData.getFloor()) == 0
						)
						&&
						(
							realEstateCadastralData.getFloor().compareTo(searchCadastralFilter.getFloorRange().getMaxFloor()) == -1 ||
							realEstateCadastralData.getFloor().compareTo(searchCadastralFilter.getFloorRange().getMaxFloor()) == 0
						)
					)
			)
				cadastralFilterRealEstates.add(realEstate);
		});
		
		return cadastralFilterRealEstates;
	}
}
