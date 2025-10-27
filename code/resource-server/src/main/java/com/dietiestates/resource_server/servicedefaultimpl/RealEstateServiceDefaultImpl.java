package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.factory.RealEstateFactory;
import com.dietiestates.resource_server.finder.CadastralDataFinder;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.model.*;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import com.dietiestates.resource_server.service.RealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RealEstateServiceDefaultImpl implements RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final RealEstateFactory realEstateFactory;
	private final RealEstateFinder realEstateFinder;
	private final RealEstateMapper realEstateMapper;
	
	private final EstateAgentFinder estateAgentFinder;
	private final CadastralDataFinder cadastralDataFinder;
	private final DetailFinder detailFinder;
	
	@Override
	public RealEstateResponse createRealEstate(RealEstateRequest request) {
		
		var realEstateSpec = realEstateMapper.toSpec(request);
		
		var estateAgent = estateAgentFinder.getEstateAgentByEmail(realEstateSpec.getEstateAgentEmail());
		var cadastralData = cadastralDataFinder.getCadastralDataById(realEstateSpec.getCadastralDataId());
		var detail = detailFinder.getDetailById(realEstateSpec.getDetailId());
		
		var realEstate = realEstateFactory.createRealEstateFromSpec(realEstateSpec, estateAgent, cadastralData, detail);
		realEstateRepository.save(realEstate);
		
		return realEstateMapper.fromEntity(realEstate);
	}
	
	@Override
	public List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates) {
		
		var response = new ArrayList<RealEstateResponse>();
		
		realEstates.forEach(realEstate -> {
			var realEstateResponse = realEstateMapper.fromEntity(realEstate);
			response.add(realEstateResponse);
		});
		
		return response;
	}
	
	@Override
	public RealEstateResponse getRealEstateById(Long id) {
		var realEstate = realEstateFinder.getRealEstateById(id);
		return realEstateMapper.fromEntity(realEstate);
	}
	
	@Override
	public List<RealEstate> getRealEstatesBySearchFilter(Search search){
		
		var allRealEstates = realEstateFinder.getAllRealEstates();
		
		var realEstatesByGeographicalPosition = getRealEstatesByGeographicalPosition(search.getDetail().getGeographicalPosition(), allRealEstates);
		var realEstatesByUtility = getRealEstatesByUtility(search.getDetail().getUtility(), realEstatesByGeographicalPosition);
		var realEstatesByCadastralFilter = getRealEstatesByCadastralFilter(search.getCadastralFilter(), realEstatesByUtility);
		
		return realEstatesByCadastralFilter;
	}

	@Override
	public List<RealEstate> getRealEstatesByGeographicalPosition(GeographicalPosition geographicalPosition, List<RealEstate> realEstates){
		var realEstatesByGeographicalPosition = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateGeographicalPosition = realEstate.getDetail().getGeographicalPosition();
			if(
					realEstateGeographicalPosition.getCity().equals(geographicalPosition.getCity()) &&
					realEstateGeographicalPosition.getMunicipality().equals(geographicalPosition.getMunicipality())
				)
				realEstatesByGeographicalPosition.add(realEstate);
		});
		
		return realEstatesByGeographicalPosition;
	}
	
	@Override
	public List<RealEstate> getRealEstatesByUtility(Utility utility, List<RealEstate> realEstates){
		var realEstatesByUtility = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateUtility = realEstate.getDetail().getUtility();
			if(
					realEstateUtility.getHasAirConditioning().equals(utility.getHasAirConditioning()) &&
					realEstateUtility.getHasDoorman().equals(utility.getHasDoorman()) &&
					realEstateUtility.getHasElevator().equals(utility.getHasElevator())
				)
				realEstatesByUtility.add(realEstate);
		});
		
		return realEstatesByUtility;
	}
	
	@Override
	public List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter,  List<RealEstate> realEstates){
		var cadastralFilterRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
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

}
