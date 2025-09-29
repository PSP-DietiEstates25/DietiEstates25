package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.GeographicalPositionDto;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.GeographicalPositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	private final DetailRepository detailRepository;
	
	public GeographicalPosition createGeographicalPosition(GeographicalPositionDto request, Long detailId) {
		var geographicalPosition = of(request, detailId);
		return geographicalPositionRepository.save(geographicalPosition);
	}
	
	public GeographicalPosition of(GeographicalPositionDto request, Long detailId) {
		
		var detail = detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
		
		return GeographicalPosition.geographicalPositionBuilder()
				.createdDate(LocalDateTime.now())
				.city(request.getCity())
				.municipality(request.getMunicipality())
				.address(request.getAddress())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.radius(request.getRadius())
				.detail(detail)
				.build();
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
