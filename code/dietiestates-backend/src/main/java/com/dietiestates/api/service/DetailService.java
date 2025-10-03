package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.mapper.DetailMapper;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

	private final DetailRepository detailRepository;
	private final DetailMapper detailMapper;
	
	private final RealEstateService realEstateService;
	private final SearchService searchService;
	
	public void createDetail(DetailRequest request) {
		
		RealEstate realEstate = request.getRealEstateId() != null ? realEstateService.getRealEstateById(request.getRealEstateId()) : null;
		Search search = request.getSearchId() != null ? searchService.getSearchById(request.getSearchId()) : null;
		
		var detail = detailMapper.toEntity(request, realEstate, search);
		detailRepository.save(detail);
	}
	
	public DetailResponse getDetail(Long detailId) {
		var detail = this.getDetailById(detailId);
		return detailMapper.fromEntity(detail);
	} 
	
	public Detail getDetailById(Long detailId) {
		return detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
	}
}
