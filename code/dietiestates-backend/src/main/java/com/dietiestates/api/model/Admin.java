package com.dietiestates.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("admin")
@EntityListeners(AuditingEntityListener.class)
public class Admin extends User {
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "admin_id",
			foreignKey = @ForeignKey(name = "ADMIN_ADMIN_ID_FK")
			)
	private Admin admin;
	
	@Builder(builderMethodName = "adminBuilder")
	public Admin(
			Long id,
			String email,
			String password,
			boolean accountLocked,
			boolean enabled,
			List<Role> roles,
			LocalDateTime createdDate,
			LocalDateTime lastModifiedDate,
			Admin admin
	) {
		super(id, email, password, accountLocked, enabled, roles, createdDate, lastModifiedDate);
		this.admin = admin;
	}
	
	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstateAgent> estateAgents = new ArrayList<>();
	
	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Admin> admins = new ArrayList<>();
	
	public void addEstateAgent(EstateAgent estateAgent) {
		estateAgents.add(estateAgent);
		estateAgent.setAdmin(this);	
	}
	
	public void addAdmin(Admin admin) {
		admins.add(admin);
		admin.setAdmin(this);
	}
}
