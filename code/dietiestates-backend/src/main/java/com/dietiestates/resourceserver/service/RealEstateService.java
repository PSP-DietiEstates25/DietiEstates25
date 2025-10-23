package com.dietiestates.resourceserver.service;

import java.util.List;

import com.dietiestates.resourceserver.dto.request.RealEstateRequest;
import com.dietiestates.resourceserver.dto.response.RealEstateResponse;
import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.Utility;

public interface RealEstateService {

	RealEstateResponse createRealEstate(RealEstateRequest request);
	
	List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates);
	
	RealEstateResponse getRealEstateById(Long id);
	
	List<RealEstate> getRealEstatesBySearchFilter(Search search);
	
	List<RealEstate> getRealEstatesByGeographicalPosition(GeographicalPosition geographicalPosition, List<RealEstate> realEstates);

	List<RealEstate> getRealEstatesByUtility(Utility utility, List<RealEstate> realEstates);
	
	List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter,  List<RealEstate> realEstates);
	
	
}
