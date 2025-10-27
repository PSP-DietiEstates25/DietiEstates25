package com.dietiestates.resource_server.finder;

import java.util.List;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.Utility;

public interface RealEstateFinder {

	RealEstate getRealEstateById(Long id)
			throws RealEstateNotFoundException;
	
	/* DA METTERE IN RealEstateServiceImpl e interfaccia
	List<RealEstate> getRealEstatesBySearchFilters(Search search);
	
	List<RealEstate> getFilteredRealEstatesByGeographicalPosition(
			GeographicalPosition geographicalPosition,
			List<RealEstate> realEstates
			);
	
	List<RealEstate> getFilteredRealEstatesByUtility(
			Utility utility,
			List<RealEstate> realEstates
			);
	
	List<RealEstate> getFilteredRealEstatesByCadastralFilter(
			CadastralFilter cadastralFilter,
			List<RealEstate> realEstates);
	*/
	
	List<RealEstate> getAllRealEstates();
}
