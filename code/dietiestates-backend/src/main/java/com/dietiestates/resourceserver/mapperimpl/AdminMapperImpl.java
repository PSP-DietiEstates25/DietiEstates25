package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.dto.response.AdminResponse;
import com.dietiestates.resourceserver.mapper.AdminMapper;
import com.dietiestates.resourceserver.mapper.StafferMapper;
import com.dietiestates.resourceserver.model.Admin;
import com.dietiestates.resourceserver.spec.StafferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminMapperImpl implements AdminMapper {
		
	private final StafferMapper stafferMapper;
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.stafferSpecBuilder()
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
