package com.dietiestates25.backend.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Staffer {
	
	@Id
	private String email;
	
	private String password;
	
	private StafferRole role;
	
}
