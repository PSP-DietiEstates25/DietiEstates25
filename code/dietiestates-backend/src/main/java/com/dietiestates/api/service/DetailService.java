package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.mapper.DetailMapper;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

	private final DetailRepository detailRepository;
	private final DetailMapper detailMapper;
	private final RealEstateRepository realEstateRepository;
	private final SearchRepository searchRepository;
	
	public Detail createDetail(DetailRequest request) {
		
		RealEstate realEstate = null;
		Search search = null;
		
		if(request.getRealEstateId() != null) {
			realEstate = realEstateRepository.findById(request.getRealEstateId())
					.orElseThrow(RealEstateNotFoundException::new);
		}
		
		if(request.getSearchId() != null) {
			search = searchRepository.findById(request.getSearchId())
					.orElseThrow(SearchNotFoundException::new);
		}
		
		var detail = detailMapper.toEntity(request, realEstate, search);
		
		return detailRepository.save(detail);
	}
	
	public Detail getDetailById(Long detailId) {
		return detailRepository.findById(detailId)
				.orElseThrow(DetailNotFoundException::new);
	}
}
