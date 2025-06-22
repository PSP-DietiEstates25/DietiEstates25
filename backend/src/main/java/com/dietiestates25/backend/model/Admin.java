package com.dietiestates25.backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
@Table(name = "admin")
public class Admin extends Staffer {
	
    @OneToMany(mappedBy = "nominatedBy", cascade = CascadeType.ALL)
    private Set<EstateAgent> nominatedAgents = new HashSet<>();

}
