package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Admin extends Staffer {
	
	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Staffer> staffers = new ArrayList<>();
	
	@Builder(builderMethodName = "builder")
	public Admin(
			DefaultAccount securityAccountDecorator,
			Admin admin
			) {
		super(securityAccountDecorator, admin);
	}
	
	public void addStaffer(Staffer staffer) {
		staffers.add(staffer);
		staffer.setAdmin(this);
	}

}
