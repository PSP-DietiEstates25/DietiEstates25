package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.CreatedStaffersResponse;
import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.mapper.StafferMapper;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Staffer;
import com.dietiestates.resource_server.spec.StafferSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StafferMapperDefaultImpl implements StafferMapper {

    @Override
    public StafferSpec toSpec(StafferRequest request){
        return StafferSpec.builder()
                .email(request.getEmail())
                .build();
    }
	
	@Override
	public StafferResponse fromEntity(Staffer staffer) {
		return StafferResponse.stafferResponseBuilder()
				.id(staffer.getId())
				.email(staffer.getEmail())
				.adminEmail(staffer.getAdmin().getEmail())
				.build();
	}

    @Override
    public CreatedStaffersResponse fromStaffers(Page<Admin> createdAdmins, Page<EstateAgent> createdEstateAgents) {
        return CreatedStaffersResponse.builder()
                .createdAdmins(createdAdmins)
                .createdEstateAgents(createdEstateAgents)
                .build();
    }
}
