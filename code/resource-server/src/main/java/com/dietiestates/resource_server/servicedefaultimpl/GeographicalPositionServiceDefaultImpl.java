package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.GeographicalPositionRequest;
import com.dietiestates.resource_server.dto.response.GeographicalPositionResponse;
import com.dietiestates.resource_server.factory.GeographicalPositionFactory;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.finder.GeographicalPositionFinder;
import com.dietiestates.resource_server.mapper.GeographicalPositionMapper;
import com.dietiestates.resource_server.repository.GeographicalPositionRepository;
import com.dietiestates.resource_server.service.GeographicalPositionService;
import com.dietiestates.resource_server.verifier.GeographicalPositionVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GeographicalPositionServiceDefaultImpl implements GeographicalPositionService {

	private final GeographicalPositionRepository geographicalPositionRepository;
	private final GeographicalPositionFactory geographicalPositionFactory;
	private final GeographicalPositionFinder geographicalPositionFinder;
	private final GeographicalPositionMapper geographicalPositionMapper;
    private final GeographicalPositionVerifier geographicalPositionVerifier;

    private final DetailFinder detailFinder;

	@Override
	public GeographicalPositionResponse createGeographicalPosition(GeographicalPositionRequest request) {
		var geographicalPositionSpec = geographicalPositionMapper.toSpec(request);

		var geographicalPosition = geographicalPositionFactory.createGeographicalPositionFromSpec(geographicalPositionSpec);
		geographicalPositionRepository.save(geographicalPosition);
		
		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}
	
	@Override
	public GeographicalPositionResponse getGeographicalPositionById(Long geographicalPositionId) {
		var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(geographicalPositionId);
		return geographicalPositionMapper.fromEntity(geographicalPosition);
	}

    @Override
    public GeographicalPositionResponse getDetailGeograpicalPosition(Long detailId) {
        geographicalPositionVerifier.checkGeographicalPositionOwnedByDetail(detailId);
        var detail = detailFinder.getGeographicalPositionDetail(detailId);
        var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(detail.getGeographicalPosition().getId());

        return geographicalPositionMapper.fromEntity(geographicalPosition);
    }

    @Override
    @Transactional
    public void updateGeographicalPosition(Long id, GeographicalPositionRequest request) {

        var geographicalPositionToUpdate = geographicalPositionFinder.getGeographicalPositionById(id);
        geographicalPositionToUpdate.setAddress(request.getAddress());
        geographicalPositionToUpdate.setCity(request.getCity());
        geographicalPositionToUpdate.setMunicipality(request.getMunicipality());
        geographicalPositionToUpdate.setLatitude(request.getLatitude());
        geographicalPositionToUpdate.setLongitude(request.getLongitude());
        geographicalPositionToUpdate.setRadius(request.getRadius());

        geographicalPositionRepository.save(geographicalPositionToUpdate);
    }
}
