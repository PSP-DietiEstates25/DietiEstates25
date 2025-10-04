package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.spec.DetailSpec;

@Component
public class DetailMapper {

	public Detail toEntity(DetailRequest request, RealEstate realEstate, Search search) {
		return Detail.detailBuilder()
				.createdDate(LocalDateTime.now())
				.realEstate(realEstate)
				.search(search)
				.build();
	}
	
	public DetailSpec toSpec(DetailRequest request) {
		return DetailSpec.builder()
				.realEstateId(request.getRealEstateId())
				.searchId(request.getSearchId())
				.build();
	}
	
	public DetailResponse fromEntity(Detail detail) {
		
		var detailResponse = DetailResponse.builder()
				.id(detail.getId())
				.createdDate(detail.getCreatedDate())
				.lastModifiedDate(detail.getLastModifiedDate())
				.geographicalPositionId(detail.getGeographicalPosition().getId())
				.utilityId(detail.getUtility().getId())
				.build();
		
		if(detail.getSearch() != null)
			detailResponse.setSearchId(detail.getSearch().getId());
		
		if(detail.getRealEstate() != null)
			detailResponse.setSearchId(detail.getRealEstate().getId());
		
		return detailResponse;
	}
}
