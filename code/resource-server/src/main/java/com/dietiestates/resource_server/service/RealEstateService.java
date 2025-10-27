package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.*;

import java.util.List;

public interface RealEstateService {

	RealEstateResponse createRealEstate(RealEstateRequest request);
	
	List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates);
	
	RealEstateResponse getRealEstateById(Long id);
	
	List<RealEstate> getRealEstatesBySearchFilter(Search search);
	
	List<RealEstate> getRealEstatesByGeographicalPosition(GeographicalPosition geographicalPosition, List<RealEstate> realEstates);

	List<RealEstate> getRealEstatesByUtility(Utility utility, List<RealEstate> realEstates);
	
	List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter,  List<RealEstate> realEstates);
	
	
}
