package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Detail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "services_id",
			foreignKey = @ForeignKey(name = "SERVICES_ID_FK"))
	private Services services;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "geographical_position_id",
			foreignKey = @ForeignKey(name = "GEOGRAPHICAL_POSITION_ID_FK"))
	private GeographicalPosition geographicalPosition;
	
	@OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Search> searches = new ArrayList<>();
	
	@OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealEstate> ads = new ArrayList<>();
	
	public void addSearch(Search search) {
		searches.add(search);
		search.setDetail(this);
	}
	
	public void addAd(RealEstate ad) {
		ads.add(ad);
		ad.setDetail(this);
	}
}
