package com.dietiestates.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.CreateDetailRequest;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.Services;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.GeographicalPositionRepository;
import com.dietiestates.api.repository.ServicesRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

    private final DetailRepository detailRepository;
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
}
