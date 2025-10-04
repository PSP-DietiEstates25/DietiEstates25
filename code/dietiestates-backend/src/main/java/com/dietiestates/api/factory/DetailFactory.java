package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.model.Detail;

public interface DetailFactory {

	Detail createDetail(DetailRequest request);
}
