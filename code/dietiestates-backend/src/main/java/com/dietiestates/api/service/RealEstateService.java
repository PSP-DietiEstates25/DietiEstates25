package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final EstateAgentRepository estateAgentRepository;
	
	private final DetailService detailService;
	private final GeographicalPositionService geeographicalPositionService;
	private final UtilityService utilityService;
	private final CadastralDataService cadastralDataService;
	
	public void createRealEstate(RealEstateDto request) {
		var realEstate = of(request);
		realEstateRepository.save(realEstate);
	}
	
	public RealEstate of(RealEstateDto request) {
		
		var estateAgent = estateAgentRepository.findByEmail(request.getEstateAgentEmail())
				.orElseThrow(EstateAgentNotFoundException::new);
		
		return RealEstate.builder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.images(request.getImages())
				.description(request.getDescription())
				.estateAgent(estateAgent)
				.build();
	}
	
	public List<RealEstate> getAllRealEstates(){
		
		var realEstatesIterable = realEstateRepository.findAll();
		var allRealEstates = new ArrayList<RealEstate>();
		realEstatesIterable.forEach(allRealEstates::add);
		
		return allRealEstates;
	}
	
	public List<RealEstate> getSearchRealEstates(Search search){
		
		var allRealEstates = this.getAllRealEstates();
		
		var geographicalPositionRealEstates = this.getGeographicalPositionRealEstates(search.getDetail().getGeographicalPosition(), allRealEstates);
		var utilityRealEstates = this.getUtilityRealEstates(search.getDetail().getUtility(), geographicalPositionRealEstates);
		var cadastralFilterRealEstates = this.getCadastralFilterRealEstates(search.getCadastralFilter(), utilityRealEstates);
		
		return cadastralFilterRealEstates;
	}
	
	public List<RealEstate> getGeographicalPositionRealEstates(GeographicalPosition searchGeographicalPosition, List<RealEstate> realEstates){
		var geographicalPositionRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateGeographicalPosition = realEstate.getDetail().getGeographicalPosition();
			if(
					realEstateGeographicalPosition.getCity() == searchGeographicalPosition.getCity() &&
					realEstateGeographicalPosition.getMunicipality() == searchGeographicalPosition.getMunicipality()
				)
				geographicalPositionRealEstates.add(realEstate);
		});
		
		return geographicalPositionRealEstates;
	}
	
	public List<RealEstate> getUtilityRealEstates(Utility searchUtility, List<RealEstate> realEstates){
		var utilityRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateUtility = realEstate.getDetail().getUtility();
			if(
					realEstateUtility.getHasAirConditioning() == searchUtility.getHasAirConditioning() &&
					realEstateUtility.getHasDoorman() == searchUtility.getHasDoorman() &&
					realEstateUtility.getHasElevator() == searchUtility.getHasElevator()
				)
				utilityRealEstates.add(realEstate);
		});
		
		return utilityRealEstates;
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
