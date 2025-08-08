package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DiscriminatorValue(value = "admin")
public class Admin extends Staffer {

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "eligedBy_admin_email",
			foreignKey = @ForeignKey(name = "ELIGEDBY_ADMIN_EMAIL_FK"))
	private Admin eligedByAdmin;
	
	@NotNull
	@OneToMany(mappedBy = "eligedByAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Admin> eligedAdmins = new ArrayList<>();
	
	@NotNull
	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstateAgent> estateAgents = new ArrayList<>();
	
	public void addEstataeAgent(EstateAgent estateAgent) {
		estateAgents.add(estateAgent);
		estateAgent.setAdmin(this);
	}
	
	public void addAdmin(Admin eligedAdmin) {
		eligedAdmins.add(eligedAdmin);
		eligedAdmin.setEligedByAdmin(this);
	}
}
