package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.mapper.DetailMapper;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.spec.DetailSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailMapperImpl implements DetailMapper {
	
	@Override
	public DetailSpec toSpec(DetailRequest request) {
		return DetailSpec.builder()
				.realEstateId(request.getRealEstateId())
				.searchId(request.getSearchId())
				.build();
	}
	
	@Override
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
