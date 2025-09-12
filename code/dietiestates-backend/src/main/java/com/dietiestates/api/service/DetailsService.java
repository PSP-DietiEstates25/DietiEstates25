package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.DetailsDto;
import com.dietiestates.api.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.api.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.api.exception.notfound.UtilityNotFoundException;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.repository.CadastralDataRepository;
import com.dietiestates.api.repository.DetailsRepository;
import com.dietiestates.api.repository.GeographicalPositionRepository;
import com.dietiestates.api.repository.UtilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailsService {

	private final DetailsRepository detailsRepository;
	private final GeographicalPositionRepository geographicalPositionRepository;
	private final CadastralDataRepository cadastralDataRepository;
	private final UtilityRepository utilityRepository;
	
	public Detail createDetails(DetailsDto request) {
		var details = of(request);
		detailsRepository.save(details);
		return details;
	}
	
	public Detail of(DetailsDto request) {
		
		var geographicalPosition = geographicalPositionRepository.findById(request.getGeographicalPositionId())
				.orElseThrow(GeographicalPositionNotFoundException::new);
		
		var cadastralData = cadastralDataRepository.findById(request.getCadastralDataId())
				.orElseThrow(CadastralDataNotFoundException::new);
		
		var utility = utilityRepository.findById(request.getUtilityId())
				.orElseThrow(UtilityNotFoundException::new);
		
		return Detail.builder()
				.createdDate(LocalDateTime.now())
				.geographicalPosition(geographicalPosition)
				.cadastralData(cadastralData)
				.utility(utility)
				.build();
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
