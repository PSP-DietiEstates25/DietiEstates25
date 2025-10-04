package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.spec.DetailSpec;

public interface DetailMapper {

	DetailSpec toSpec(DetailRequest request);
	
	DetailResponse fromEntity(Detail detail);
}
