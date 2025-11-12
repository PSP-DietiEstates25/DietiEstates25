package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.CreatedStaffersResponse;
import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Staffer;
import com.dietiestates.resource_server.spec.StafferSpec;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StafferMapper {
    StafferSpec toSpec(StafferRequest request);
    StafferResponse fromEntity(Staffer staffer);
	CreatedStaffersResponse fromStaffers(Page<Admin> createdAdmins, Page<EstateAgent> createdEstateAgents);
}
