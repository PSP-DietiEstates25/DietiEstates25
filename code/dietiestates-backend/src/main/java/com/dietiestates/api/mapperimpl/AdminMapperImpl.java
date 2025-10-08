package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.dto.response.AdminResponse;
import com.dietiestates.api.mapper.AccountMapper;
import com.dietiestates.api.mapper.AdminMapper;
import com.dietiestates.api.mapper.StafferMapper;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.spec.StafferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminMapperImpl implements AdminMapper {
	
	private final AccountMapper accountMapper;
	private final StafferMapper stafferMapper;
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.stafferSpecBuilder()
				.email(request.getEmail())
				.password(request.getPassword())
				.accountLocked(false)
				.enabled(true)
				.adminEmail(request.getAdminEmail())
				.build();
	}

	@Override
	public AdminResponse fromEntity(Admin admin) {
		return AdminResponse.adminResponseBuilder()
				.id(admin.getId())
				.account(accountMapper.fromEntity(admin.getSecurityAccountDecorator()))
				.adminEmail(admin.getAdmin().getSecurityAccountDecorator().getAccountEmail())
				.createdStaffers(stafferMapper.createStaffersResponse(admin.getStaffers()))
				.build();
	}
	
}
