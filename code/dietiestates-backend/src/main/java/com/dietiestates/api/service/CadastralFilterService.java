package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.response.CadastralFilterResponse;
import com.dietiestates.api.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.api.exception.notowned.CadastralFilterNotOwnedBySearchException;
import com.dietiestates.api.mapper.CadastralFilterMapper;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.CadastralFilterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralFilterService {

	private final CadastralFilterRepository cadastralFilterRepository;
	private final CadastralFilterMapper cadastralFilterMapper;
	
	private final SearchService searchService;
	
	public CadastralFilter createCadastralFilter(CadastralFilterRequest request, Long searchId) {
		
		var search = searchService.getSearchById(searchId);
		
		var cadastralFilter = cadastralFilterMapper.toEntity(request, search);
		return cadastralFilterRepository.save(cadastralFilter);
	}
	
	public CadastralFilterResponse getCadastralFilter(Long searchId, Long cadastralFilterId) {
		
		var cadastralFilter = this.getCadastralFilterById(cadastralFilterId);
		var search = searchService.getSearchById(searchId);
		
		this.checkCadastralFilterOwnedBySearch(cadastralFilter.getSearch().getId(), search.getId());
		
		return cadastralFilterMapper.fromEntity(cadastralFilter);
	}
	
	public CadastralFilter getCadastralFilterById(Long id) {
		return cadastralFilterRepository.findById(id)
				.orElseThrow(CadastralFilterNotFoundException::new);
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
	
	public void checkCadastralFilterOwnedBySearch(Long cadastralFilterId, Long searchId) {
		
		if(!cadastralFilterId.equals(searchId))
			throw new CadastralFilterNotOwnedBySearchException();
	}
}
