package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.DetailDto;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

	private final DetailRepository detailRepository;
	private final RealEstateRepository realEstateRepository;
	private final SearchRepository searchRepository;
	
	public Detail createDetail(DetailDto request) {
		var detail = of(request);
		detailRepository.save(detail);
		return detail;
	}
	
	public Detail of(DetailDto request) {
		
		RealEstate realEstate = null;
		Search search = null;
		
		if(request.getRealEstateId() != null) {
			realEstate = realEstateRepository.findById(request.getRealEstateId())
					.orElseThrow(RealEstateNotFoundException::new);
		}
		
		if(request.getSearchId() != null) {
			search = searchRepository.findById(request.getSearchId())
					.orElseThrow(SearchNotFoundException::new);
		}
		
		return Detail.detailBuilder()
				.createdDate(LocalDateTime.now())
				.search(search)
				.realEstate(realEstate)
				.build();
	}
	
	/*
    private final DetailRepository detailRepository;
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
