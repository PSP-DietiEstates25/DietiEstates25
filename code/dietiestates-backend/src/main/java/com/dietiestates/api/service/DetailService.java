package com.dietiestates.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.CreateDetailRequest;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.Services;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.GeographicalPositionRepository;
import com.dietiestates.api.repository.ServicesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

    private final DetailRepository detailRepository;
    private final ServicesRepository servicesRepository;
    private final GeographicalPositionRepository geoRepository;

    @Transactional
    public Long create(CreateDetailRequest req) {
        // Services
        Services s = new Services();
        s.setHasAirConditioning(req.services().hasAirConditioning());
        s.setHasDoorman(req.services().hasDoorman());
        s.setHasElevator(req.services().hasElevator());
        s = servicesRepository.save(s);

        // Geo
        GeographicalPosition g = new GeographicalPosition();
        g.setCity(req.geo().city());
        g.setMunicipality(req.geo().municipality());
        g.setZoneMarkerLatitude(req.geo().zoneMarkerLatitude());
        g.setZoneMarkerLongitude(req.geo().zoneMarkerLongitude());
        g.setZoneMarkerRadius(req.geo().zoneMarkerRadius());
        g = geoRepository.save(g);

        // Detail (ManyToOne verso Services e GeographicalPosition)
        Detail d = new Detail();
        d.setServices(s);
        d.setGeographicalPosition(g);

        return detailRepository.save(d).getId();
    }
}
