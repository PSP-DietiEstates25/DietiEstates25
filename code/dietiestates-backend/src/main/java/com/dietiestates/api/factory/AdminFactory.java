package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.StafferRequest;

public interface AdminFactory extends AuthenticationFactory {

	void register(StafferRequest request);
}
