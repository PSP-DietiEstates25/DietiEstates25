package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.AdminResponse;
import com.dietiestates.resource_server.mapper.AdminMapper;
import com.dietiestates.resource_server.mapper.StafferMapper;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.spec.StafferSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminMapperDefaultImpl implements AdminMapper {
		
	private final StafferMapper stafferMapper;
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.builder()
				.email(request.getEmail())
				.adminEmail(request.getAdminEmail())
				.build();
	}

	@Override
	public AdminResponse fromEntity(Admin admin) {
		return AdminResponse.adminResponseBuilder()
				.id(admin.getId())
				.email(admin.getEmail())
				.adminEmail(admin.getAdmin().getEmail())
				.createdStaffers(stafferMapper.createStaffersResponse(admin.getStaffers()))
				.build();
	}
	
}
