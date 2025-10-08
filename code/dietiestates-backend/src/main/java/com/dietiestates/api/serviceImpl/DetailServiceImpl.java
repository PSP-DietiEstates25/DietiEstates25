package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.factory.DetailFactory;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.GeographicalPositionFinder;
import com.dietiestates.api.finder.UtilityFinder;
import com.dietiestates.api.mapper.DetailMapper;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.service.DetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailServiceImpl implements DetailService {

	private final DetailRepository detailRepository;
	private final DetailFactory detailFactory;
	private final DetailFinder detailFinder;
	private final DetailMapper detailMapper;
	
	private final GeographicalPositionFinder geographicalPositionFinder;
	private final UtilityFinder utilityFinder;
	
	@Override
	public void createDetail(DetailRequest request) {
		
		var detailSpec = detailMapper.toSpec(request);
		
		var geographicalPosition = geographicalPositionFinder.getGeographicalPositionById(detailSpec.getGeographicalPositionId());
		var utility = utilityFinder.getUtilityById(detailSpec.getUtilityId());
		
		var detail = detailFactory.createDetailFromSpec(detailSpec, geographicalPosition, utility);
		detailRepository.save(detail);
	}
	
	@Override
	public DetailResponse getDetailById(Long detailId) {
		var detail = detailFinder.getDetailById(detailId);
		return detailMapper.fromEntity(detail);
	} 
	
}
