package com.dietiestates.api.service;

import java.util.List;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.Utility;

public interface RealEstateService {

	void createRealEstate(RealEstateRequest request);
	
	List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates);
	
	List<RealEstate> getRealEstatesBySearchFilter(Search search);
	
	List<RealEstate> getRealEstatesByGeographicalPosition(GeographicalPosition geographicalPosition, List<RealEstate> realEstates);

	List<RealEstate> getRealEstatesByUtility(Utility utility, List<RealEstate> realEstates);
	
	List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter,  List<RealEstate> realEstates);
	
	
}
