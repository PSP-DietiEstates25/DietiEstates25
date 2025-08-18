package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "admin")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@ManyToOne
	@JoinColumn(name = "created_by_admin_id", foreignKey = @ForeignKey(name = "FK_ADMIN_CREATED_BY"))
	private Admin createdBy;

	@NotNull
	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Admin> createdAdmins = new ArrayList<>();

	@NotNull
	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstateAgent> estateAgents = new ArrayList<>();

	public void addEstateAgent(EstateAgent estateAgent) {
		estateAgents.add(estateAgent);
		estateAgent.setAdmin(this);
	}

	public void addAdmin(Admin child) {
		createdAdmins.add(child);
		child.setCreatedBy(this);
	}
}
