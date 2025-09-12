package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.DetailsDto;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailsRepository;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailsService {

	private final DetailsRepository detailsRepository;
	private final RealEstateRepository realEstateRepository;
	private final SearchRepository searchRepository;
	
	public Detail createDetails(DetailsDto request) {
		var details = of(request);
		detailsRepository.save(details);
		return details;
	}
	
	public Detail of(DetailsDto request) {
		
		Optional<RealEstate> realEstate = null;
		Optional<Search> search = null;
		Detail detail = Detail.builder().createdDate(LocalDateTime.now()).build();
		
		if(request.getRealEstateId() != null) {
			realEstate = realEstateRepository.findById(request.getRealEstateId());
			detail.setRealEstate(realEstate.orElse(null));
		}
		
		if(request.getSearchId() != null) {
			search = searchRepository.findById(request.getSearchId());
			detail.setSearch(search.orElse(null));
		}
		
		return detail;
	}
	
	/*
    private final DetailsRepository detailRepository;
    private final UtilityRepository servicesRepository;
    private final GeographicalPositionRepository geoRepository;

    @Autowired
    private ApplicationContext context;
    @Transactional
    public Long create(@Valid CreateDetailRequest req) {
        Utility services = Utility.builder()
                .hasAirConditioning(req.services().hasAirConditioning())
                .hasDoorman(req.services().hasDoorman())
                .hasElevator(req.services().hasElevator())
                .build();
        services = servicesRepository.save(services);

        GeographicalPosition geo = GeographicalPosition.builder()
                .city(req.geo().city())
                .municipality(req.geo().municipality())
                .zoneMarkerLatitude(req.geo().zoneMarkerLatitude())
                .zoneMarkerLongitude(req.geo().zoneMarkerLongitude())
                .zoneMarkerRadius(req.geo().zoneMarkerRadius())
                .build();
        geo = geoRepository.save(geo);

        Detail detail = Detail.builder()
                .services(services)
                .geographicalPosition(geo)
                .build();

        return detailRepository.save(detail).getId();
    }
    */
}
