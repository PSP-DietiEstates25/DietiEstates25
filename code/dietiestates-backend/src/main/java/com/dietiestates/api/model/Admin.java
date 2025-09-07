package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Admin extends User {
	
	@ManyToOne()
	private Admin admin;
	
	@OneToMany()
	private List<EstateAgent> estateAgents = new ArrayList<>();
	
	@OneToMany()
	private List<Admin> admins = new ArrayList<>();
}
