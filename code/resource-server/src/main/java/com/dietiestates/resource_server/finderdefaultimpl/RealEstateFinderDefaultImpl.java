package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.SearchRealEstateFinder;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.SearchRealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import com.dietiestates.resource_server.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RealEstateFinderDefaultImpl implements RealEstateFinder {

	private final RealEstateRepository realEstateRepository;
    private final SearchRealEstateFinder searchRealEstateFinder;
    private final PageUtils pageUtils;
	
	@Override
	public RealEstate getRealEstateById(Long id) throws RealEstateNotFoundException {
		return realEstateRepository.findById(id)
				.orElseThrow(RealEstateNotFoundException::new);
	}

    @Override
    public Page<RealEstate> getEstateAgentRealEstates(Long estateAgentId, Pageable pageable) {
        return realEstateRepository.findByEstateAgentId(estateAgentId, pageable);
    }

    @Override
    public Page<RealEstate> getAdminRealEstates(Admin admin, Pageable pageable) {
        var estateAgentsRealEstates = getAdminEstateAgentsRealEstates(admin);
        return PageUtils.toPage(estateAgentsRealEstates, pageable);
    }

    @Override
    public Page<RealEstate> getSearchRealEstates(Long searchId, Pageable pageable) {
        var searchSearchRealEsatates = searchRealEstateFinder.getSearchSearchRealEstates(searchId, pageable);
        return searchSearchRealEsatates.map(SearchRealEstate::getRealEstate);
    }

    @Override
    public List<RealEstate> getAllRealEstates() {
        var realEstatesIterable = realEstateRepository.findAll();
        var allRealEstates = new ArrayList<RealEstate>();
        realEstatesIterable.forEach(allRealEstates::add);

        return allRealEstates;
    }

    private List<RealEstate> getAdminEstateAgentsRealEstates(Admin admin) {
        var estateAgents = admin.getCreatedEstateAgents();
        var realEstates = new ArrayList<RealEstate>();

        estateAgents.forEach(estateAgent -> {
            realEstates.addAll(estateAgent.getRealEstates());
        });

        return realEstates;
    }

	/*
	@Override
	public List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter) {
		var cadastralFilterRealEstates = new ArrayList<RealEstate>();
		var allRealEstates = this.getAllRealEstates();
		
		allRealEstates.forEach(realEstate -> {
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
	*/

}
