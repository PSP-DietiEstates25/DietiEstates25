package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.DetailRequest;
import com.dietiestates.resource_server.dto.response.DetailResponse;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.spec.DetailSpec;

public interface DetailMapper {
	DetailSpec toSpec(DetailRequest request);
	DetailResponse fromEntity(Detail detail);
}
