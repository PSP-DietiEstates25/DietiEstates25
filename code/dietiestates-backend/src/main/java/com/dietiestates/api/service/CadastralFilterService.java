package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.mapper.CadastralFilterMapper;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.CadastralFilterRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralFilterService {

	private final CadastralFilterRepository cadastralFilterRepository;
	private final CadastralFilterMapper cadastralFilterMapper;
	private final SearchRepository searchRepository;
	
	public void createCadastralFilter(CadastralFilterRequest request, Long searchId) {
		
		var search = searchRepository.findById(searchId)
				.orElseThrow(SearchNotFoundException::new);
		
		var cadastralFilter = cadastralFilterMapper.toEntity(request, search);
		
		System.out.println("==============================================================================================");
		System.out.println("Incoming cadastralFilter request: {}" + cadastralFilter.getId());
		System.out.println("Incoming cadastralFilter request: {}" + cadastralFilter.getPriceRange().toString());
		System.out.println("Incoming cadastralFilter request: {}" + cadastralFilter.getEnergyClassRange().toString());
		System.out.println("Incoming cadastralFilter request: {}" + cadastralFilter.getSquareMetersRange().toString());
		System.out.println("Incoming cadastralFilter request: {}" + cadastralFilter.getRoomsRange().toString());
		System.out.println("Incoming cadastralFilter request: {}" + cadastralFilter.getFloorRange().toString());
		System.out.println("==============================================================================================");
		
		cadastralFilterRepository.save(cadastralFilter);
	}
	
	public List<RealEstate> getCadastralFilterRealEstates(CadastralFilter searchCadastralFilter,  List<RealEstate> realEstates){
		var cadastralFilterRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateCadastralData = realEstate.getCadastralData();
			if(
					searchCadastralFilter.getPriceRange().contains(realEstateCadastralData.getPrice()) &&
					searchCadastralFilter.getSquareMetersRange().contains(realEstateCadastralData.getSquareMeters()) &&
					searchCadastralFilter.getEnergyClassRange().contains(realEstateCadastralData.getEnergyClass().getOrder()) &&
					searchCadastralFilter.getRoomsRange().contains(realEstateCadastralData.getRooms()) &&
					searchCadastralFilter.getFloorRange().contains(realEstateCadastralData.getFloor())
				)
				cadastralFilterRealEstates.add(realEstate);
		});
		
		return cadastralFilterRealEstates;
	}
}
