package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.mapper.GeographicalPositionMapper;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.GeographicalPositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	private final GeographicalPositionMapper geographicalPositionMapper;
	private final DetailRepository detailRepository;
	
	public void createGeographicalPosition(GeographicalPositionRequest request, Long detailId) {
		
		var detail = detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
		
		var geographicalPosition = geographicalPositionMapper.toEntity(request, detail);
		
		geographicalPositionRepository.save(geographicalPosition);
	}
	
	public List<RealEstate> getGeographicalPositionRealEstates(GeographicalPosition searchGeographicalPosition, List<RealEstate> realEstates){
		var geographicalPositionRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateGeographicalPosition = realEstate.getDetail().getGeographicalPosition();
			if(
					realEstateGeographicalPosition.getCity().equals(searchGeographicalPosition.getCity()) &&
					realEstateGeographicalPosition.getMunicipality().equals(searchGeographicalPosition.getMunicipality())
				)
				geographicalPositionRealEstates.add(realEstate);
		});
		
		return geographicalPositionRealEstates;
	}
}
