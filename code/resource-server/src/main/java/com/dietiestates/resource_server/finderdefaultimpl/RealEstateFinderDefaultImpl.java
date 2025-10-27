package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RealEstateFinderDefaultImpl implements RealEstateFinder {

	private final RealEstateRepository realEstateRepository;
	
	@Override
	public RealEstate getRealEstateById(Long id)
			throws RealEstateNotFoundException {
		return realEstateRepository.findById(id)
				.orElseThrow(RealEstateNotFoundException::new);
	}
	
	@Override
	public List<RealEstate> getAllRealEstates() {
		
		var realEstatesIterable = realEstateRepository.findAll();
		var allRealEstates = new ArrayList<RealEstate>();
		realEstatesIterable.forEach(allRealEstates::add);
		
		return allRealEstates;
	}

	/*
	@Override
	public List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter) {
		var cadastralFilterRealEstates = new ArrayList<RealEstate>();
		var allRealEstates = this.getAllRealEstates();
		
		allRealEstates.forEach(realEstate -> {
			var realEstateCadastralData = realEstate.getCadastralData();
			if(
					cadastralFilter.getPriceRange().contains(realEstateCadastralData.getPrice()) &&
					cadastralFilter.getSquareMetersRange().contains(realEstateCadastralData.getSquareMeters()) &&
					cadastralFilter.getEnergyClassRange().contains(realEstateCadastralData.getEnergyClass().getOrder()) &&
					cadastralFilter.getRoomsRange().contains(realEstateCadastralData.getRooms()) &&
					cadastralFilter.getFloorRange().contains(realEstateCadastralData.getFloor())
				)
				cadastralFilterRealEstates.add(realEstate);
		});
		
		return cadastralFilterRealEstates;
	}
	*/

}
