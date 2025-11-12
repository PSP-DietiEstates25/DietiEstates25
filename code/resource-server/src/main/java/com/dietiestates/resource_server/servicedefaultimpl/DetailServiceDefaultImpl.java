package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.DetailRequest;
import com.dietiestates.resource_server.dto.response.DetailResponse;
import com.dietiestates.resource_server.factory.DetailFactory;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.finder.GeographicalPositionFinder;
import com.dietiestates.resource_server.finder.UtilityFinder;
import com.dietiestates.resource_server.mapper.DetailMapper;
import com.dietiestates.resource_server.repository.DetailRepository;
import com.dietiestates.resource_server.service.DetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DetailServiceDefaultImpl implements DetailService {

	private final DetailRepository detailRepository;
	private final DetailFactory detailFactory;
	private final DetailFinder detailFinder;
	private final DetailMapper detailMapper;
	
	private final GeographicalPositionFinder geographicalPositionFinder;
	private final UtilityFinder utilityFinder;
	
	@Override
	public DetailResponse createDetail(DetailRequest request) {
		var detailSpec = detailMapper.toSpec(request);
		
		var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(detailSpec.getGeographicalPositionId());
		var utility = utilityFinder.getUtilityById(detailSpec.getUtilityId());
		
		var detail = detailFactory.createDetailFromSpec(detailSpec, geographicalPosition, utility);
		detailRepository.save(detail);
		
		return detailMapper.fromEntity(detail);
	}
	
	@Override
	public DetailResponse getDetailById(Long detailId) {
		var detail = detailFinder.getDetailById(detailId);
		return detailMapper.fromEntity(detail);
	}

    @Override
    @Transactional
    public void updateDetail(Long id, DetailRequest request) {
        var detailToUpdate = detailFinder.getDetailById(id);

        var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(request.getGeographicalPositionId());
        detailToUpdate.setGeographicalPosition(geographicalPosition);

        var utility = utilityFinder.getUtilityById(request.getUtilityId());
        detailToUpdate.setUtility(utility);

        detailRepository.save(detailToUpdate);
    }
}
