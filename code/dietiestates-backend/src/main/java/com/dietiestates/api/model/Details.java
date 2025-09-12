package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.dto.DetailsDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Details {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private final LocalDateTime createdDate = LocalDateTime.now();
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne(mappedBy="details", cascade = CascadeType.ALL, orphanRemoval = true)
	private GeographicalPosition geographicalPosition;
	
	@OneToOne(mappedBy="details", cascade = CascadeType.ALL, orphanRemoval = true)
	private Services services;
	
	@OneToOne(mappedBy="details", cascade = CascadeType.ALL, orphanRemoval = true)
	private Data data;
		
	@OneToOne
	@JoinColumn(
			nullable = true,
			name = "search_id",
			foreignKey = @ForeignKey(name = "DETAILS_SEARCH_ID_FK"))
	private Search search;
	
	@OneToOne
	@JoinColumn(
			nullable = true,
			name = "real_estate_id",
			foreignKey = @ForeignKey(name = "DETAILS_REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;
	
	public void addGeographicalPosition(GeographicalPosition geographicalPosition) {
		this.geographicalPosition = geographicalPosition;
		geographicalPosition.setDetails(this);
	}
	
	public void addServices(Services services) {
		this.services = services;
		services.setDetails(this);
	}
	
	public void addData(Data data) {
		this.data = data;
		data.setDetails(this);
	}
	
}
