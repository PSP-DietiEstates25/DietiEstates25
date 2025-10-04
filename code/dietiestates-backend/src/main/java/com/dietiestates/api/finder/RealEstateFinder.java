package com.dietiestates.api.finder;

import java.util.List;

import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.Utility;

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
