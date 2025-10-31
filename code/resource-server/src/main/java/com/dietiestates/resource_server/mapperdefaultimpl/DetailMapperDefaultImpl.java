package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.DetailRequest;
import com.dietiestates.resource_server.dto.response.DetailResponse;
import com.dietiestates.resource_server.mapper.DetailMapper;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.spec.DetailSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetailMapperDefaultImpl implements DetailMapper {
	
	@Override
	public DetailSpec toSpec(DetailRequest request) {
		return DetailSpec.builder()
				.geographicalPositionId(request.getGeographicalPositionId())
				.utilityId(request.getUtilityId())
				.build();
	}
	
	@Override
	public DetailResponse fromEntity(Detail detail) {
		
		return DetailResponse.builder()
				.id(detail.getId())
				.createdDate(detail.getCreatedDate())
				.lastModifiedDate(detail.getLastModifiedDate())
				.geographicalPositionId(detail.getGeographicalPosition().getId())
				.utilityId(detail.getUtility().getId())
				.build();
	}
}
