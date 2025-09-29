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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@DiscriminatorValue("estate_agent")
@EntityListeners(AuditingEntityListener.class)
public class EstateAgent extends User {
	
	@OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<RealEstate> realEstates = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "admin_id",
			foreignKey = @ForeignKey(name = "ESTATE_AGENT_ADMIN_ID_FK"))
	private Admin admin;
	
	@Builder(builderMethodName = "estateAgentBuilder")
	public EstateAgent(
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
	
	public void addRealEstate(RealEstate realEstate) {
		realEstates.add(realEstate);
		realEstate.setEstateAgent(this);
	}
}
