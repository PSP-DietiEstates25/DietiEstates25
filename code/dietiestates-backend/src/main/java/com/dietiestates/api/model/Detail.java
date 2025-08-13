package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Detail {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "services_id",
			foreignKey = @ForeignKey(name = "SERVICES_ID_FK"))
	private Services services;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "geographical_position_id",
			foreignKey = @ForeignKey(name = "GEOGRAPHICAL_POSITION_ID_FK"))
	private GeographicalPosition geographicalPosition;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Search> searches = new ArrayList<>();
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealEstateAd> ads = new ArrayList<>();
	
	public void addSearch(Search search) {
		searches.add(search);
		search.setDetail(this);
	}
	
	public void addAd(RealEstateAd ad) {
		ads.add(ad);
		ad.setDetail(null);
	}
}
