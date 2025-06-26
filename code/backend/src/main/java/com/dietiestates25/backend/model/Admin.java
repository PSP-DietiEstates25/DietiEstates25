package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admins")
public class Admin extends Staffer {
	
	@NotNull
	private StafferRole role = StafferRole.ADMIN;
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin nominatedByAdmin;

	@NotNull
	@OneToMany(mappedBy = "nominatedByAdmin", fetch = FetchType.LAZY)
	private List<Admin> electedAdmins = new ArrayList<>();
	
	@NotNull
    @OneToMany(mappedBy = "nominatedByAdmin", cascade = CascadeType.ALL)
    private List<EstateAgent> nominatedAgents = new ArrayList<>();

}
