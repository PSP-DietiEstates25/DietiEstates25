package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.DetailRequest;
import com.dietiestates.resourceserver.dto.response.DetailResponse;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.spec.DetailSpec;

public interface DetailMapper {

	DetailSpec toSpec(DetailRequest request);
	
	DetailResponse fromEntity(Detail detail);
}
