package com.dietiestates25.backend.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Staffer {
	
	private StafferRole role;
	
	@Embedded
	private Account account;
}
