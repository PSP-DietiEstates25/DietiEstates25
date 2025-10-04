package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.spec.StafferSpec;

public interface AdminMapper {

	StafferSpec toSpec(StafferRequest request);
}
