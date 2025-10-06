package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.factory.DetailFactory;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.RealEstateFinder;
import com.dietiestates.api.finder.SearchFinder;
import com.dietiestates.api.mapper.DetailMapper;
import com.dietiestates.api.repository.DetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

	private final DetailRepository detailRepository;
	private final DetailFactory detailFactory;
	private final DetailFinder detailFinder;
	private final DetailMapper detailMapper;
	
	private final RealEstateFinder realEstateFinder;
	private final SearchFinder searchFinder;
	
	public void createDetail(DetailRequest request) {
		
		var detailSpec = detailMapper.toSpec(request);
		
		var realEstate = request.getRealEstateId() != null ? realEstateFinder.getRealEstateById(request.getRealEstateId()) : null;
		var search = request.getSearchId() != null ? searchFinder.getSearchById(request.getSearchId()) : null;
		
		var detail = detailFactory.createDetailFromSpec(detailSpec, realEstate, search);
		detailRepository.save(detail);
	}
	
	public DetailResponse getDetail(Long detailId) {
		var detail = detailFinder.getDetailById(detailId);
		return detailMapper.fromEntity(detail);
	} 
	
}
