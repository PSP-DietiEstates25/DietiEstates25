package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.api.exception.notowned.GeographicalPositionNotOwnedByDetailException;
import com.dietiestates.api.mapper.GeographicalPositionMapper;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.GeographicalPositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	private final GeographicalPositionMapper geographicalPositionMapper;
	
	private final DetailService detailService;
	
	public void createGeographicalPosition(GeographicalPositionRequest request, Long detailId) {
		
		var detail = detailService.getDetailById(detailId);

		var geographicalPosition = geographicalPositionMapper.toEntity(request, detail);		
		geographicalPositionRepository.save(geographicalPosition);
	}
	
	public GeographicalPositionResponse getGeographicalPosition(Long detailId, Long geographicalPositionId) {
		
		var detail = detailService.getDetailById(detailId);
		var geographicalPosition = this.getGeographicalPositionById(geographicalPositionId);
		
		this.checkGeographicalPositionOwnedByDetail(detail.getId(), geographicalPosition.getDetail().getId());
		
		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}
	
	public GeographicalPosition getGeographicalPositionById(Long id) {
		return geographicalPositionRepository.findById(id)
				.orElseThrow(GeographicalPositionNotFoundException::new);
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
	
	public void checkGeographicalPositionOwnedByDetail(Long detailId, Long geographicalPositionDetailId) {
		
		if(!geographicalPositionDetailId.equals(detailId))
			throw new GeographicalPositionNotOwnedByDetailException();
	}
}
