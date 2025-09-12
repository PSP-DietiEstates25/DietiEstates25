package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.DetailsDto;
import com.dietiestates.api.model.Details;
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
	
	public Details createDetails(DetailsDto request) {
		var details = of(request);
		detailsRepository.save(details);
		return details;
	}
	
	public Details of(DetailsDto request) {
		var realEstate = realEstateRepository.findById(request.getRealEstateId());
		var search = searchRepository.findById(request.getSearchId());
		return Details.builder()
				.createdDate(LocalDateTime.now())
				.realEstate(realEstate.orElse(null))
				.search(search.orElse(null))
				.build();
	}
	
	/*
    private final DetailsRepository detailRepository;
    private final ServicesRepository servicesRepository;
    private final GeographicalPositionRepository geoRepository;

    @Autowired
    private ApplicationContext context;
    @Transactional
    public Long create(@Valid CreateDetailRequest req) {
        Services services = Services.builder()
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
