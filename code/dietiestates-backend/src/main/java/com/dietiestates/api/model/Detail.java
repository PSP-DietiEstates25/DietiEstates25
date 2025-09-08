package com.dietiestates.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
			foreignKey = @ForeignKey(name = "DETAIL_SERVICES_ID_FK"))
	private Services services;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "geographical_position_id",
			foreignKey = @ForeignKey(name = "DETAIL_GEOGRAPHICAL_POSITION_ID_FK"))
	private GeographicalPosition geographicalPosition;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Search> searches = new ArrayList<>();
	
	@OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<RealEstate> realEstates = new ArrayList<>();
	
	public void addSearch(Search search) {
		searches.add(search);
		search.setDetail(this);
	}
	
	public void addAd(RealEstate realEstate) {
		realEstates.add(realEstate);
		realEstate.setDetail(this);
	}
}
