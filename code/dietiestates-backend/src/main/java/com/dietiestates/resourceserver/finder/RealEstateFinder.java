package com.dietiestates.resourceserver.finder;

import java.util.List;

import com.dietiestates.resourceserver.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.Utility;

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
