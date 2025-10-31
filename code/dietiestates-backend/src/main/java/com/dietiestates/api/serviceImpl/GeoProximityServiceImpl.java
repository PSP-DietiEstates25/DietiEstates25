package com.dietiestates.api.serviceImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dietiestates.api.enums.ProximityTag;
import com.dietiestates.api.service.GeoProximityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoProximityServiceImpl implements GeoProximityService {

    private final RestTemplate restTemplate;

    @Value("b2995e88f8dd471e82a2d20b82846412")
    private String apiKey;

    private static final String SCHOOLS = "education.school";
    private static final String PARKS = "leisure.park";
    private static final String PUBLIC_TRANSPORT = "public_transport";

    private boolean existsAtLeastOne(String categories, double lat, double lon, int radius) {
        if (apiKey == null || apiKey.isBlank())
            return false;
        String url = String.format(
                "https://api.geoapify.com/v2/places?categories=%s&filter=circle:%f,%f,%d&limit=1&apiKey=%s",
                categories, lon, lat, radius, apiKey);
        try {
            var resp = restTemplate.getForObject(URI.create(url), Map.class);
            var features = (List<?>) resp.getOrDefault("features", List.of());
            return !features.isEmpty();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<ProximityTag> detectTags(double lat, double lon, int radiusMeters) {
        var out = new ArrayList<ProximityTag>();
        if (existsAtLeastOne(SCHOOLS, lat, lon, radiusMeters))
            out.add(ProximityTag.NEAR_SCHOOLS);
        if (existsAtLeastOne(PARKS, lat, lon, radiusMeters))
            out.add(ProximityTag.NEAR_PARKS);
        if (existsAtLeastOne(PUBLIC_TRANSPORT, lat, lon, radiusMeters))
            out.add(ProximityTag.NEAR_PUBLIC_TRANSPORT);
        return out;
    }
}
