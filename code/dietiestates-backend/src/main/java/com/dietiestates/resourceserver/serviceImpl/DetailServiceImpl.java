package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.DetailRequest;
import com.dietiestates.resourceserver.dto.response.DetailResponse;
import com.dietiestates.resourceserver.factory.DetailFactory;
import com.dietiestates.resourceserver.finder.DetailFinder;
import com.dietiestates.resourceserver.finder.GeographicalPositionFinder;
import com.dietiestates.resourceserver.finder.UtilityFinder;
import com.dietiestates.resourceserver.mapper.DetailMapper;
import com.dietiestates.resourceserver.repository.DetailRepository;
import com.dietiestates.resourceserver.service.DetailService;

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
	
}
