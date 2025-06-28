package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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
@Table(name = "geographical_positions")
public class GeographicalPosition {
	
	@EmbeddedId
	private Coordinates coordinates;
    
    @NotNull
    private String city;
    
    @NotNull
    private String municipality;
    
    @NotNull
    private double abitableRadius;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realEstate")
    private RealEstate realEstate;

    @OneToMany(mappedBy = "geographicalPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSearch> savedSearches = new ArrayList<>();

}
