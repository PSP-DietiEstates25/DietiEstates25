package com.dietiestates.api.service;

import java.util.List;

import com.dietiestates.api.enums.ProximityTag;

public interface GeoProximityService {
    
    List<ProximityTag> detectTags(double lat, double lon, int radiusMeters);
}
