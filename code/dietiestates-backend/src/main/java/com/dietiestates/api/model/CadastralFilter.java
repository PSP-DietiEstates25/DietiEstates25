package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.model.range.EnergyClassRange;
import com.dietiestates.api.model.range.FloorRange;
import com.dietiestates.api.model.range.PriceRange;
import com.dietiestates.api.model.range.RoomsRange;
import com.dietiestates.api.model.range.SquareMetersRange;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CadastralFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Embedded
	private PriceRange priceRange;
	
	@Embedded
	private SquareMetersRange squareMetersRange;
	
	@Embedded
	private EnergyClassRange energyClassRange;
	
	@Embedded
	private RoomsRange roomsRange;
	
	@Embedded
	private FloorRange floorRange;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "search_id",
			foreignKey = @ForeignKey(name = "CADASTRAL_FILTER_SEARCH_ID_FK"))
	private Search search;
	
	
	@Builder(builderMethodName = "cadastralFilterBuilder")
	public CadastralFilter(
		LocalDateTime createdDate,
		BigDecimal minPrice,
		BigDecimal maxPrice,
		Integer minSquareMeters,
		Integer maxSquareMeters,
		Integer minEnergyClass,
		Integer maxEnergyClass,
		Integer minRooms,
		Integer maxRooms,
		Integer minFloor,
		Integer maxFloor,
		Search search
		) {
		this.createdDate = createdDate;
		this.priceRange = new PriceRange(minPrice, maxPrice);
		this.squareMetersRange = new SquareMetersRange(minSquareMeters, maxSquareMeters);
		this.energyClassRange = new EnergyClassRange(minEnergyClass, maxEnergyClass);
		this.roomsRange = new RoomsRange(minRooms, maxRooms);
		this.floorRange = new FloorRange(minFloor, maxFloor);
		this.setSearch(search);
	}
	
	/*
	@Builder(builderMethodName = "cadastralFilterBuilder")
	public CadastralFilter(
		LocalDateTime createdDate,
		PriceRange priceRange,
		SquareMetersRange squareMetersRange,
		EnergyClassRange energyClassRange,
		RoomsRange roomsRange,
		FloorRange floorRange,
		Search search
		) {
		this.createdDate = createdDate;
		this.priceRange = priceRange;
		this.squareMetersRange = squareMetersRange;
		this.energyClassRange = energyClassRange;
		this.roomsRange = roomsRange;
		this.floorRange = floorRange;
		this.setSearch(search);
	}*/
	
	public void setSearch(Search search) {
		this.search = search;
		search.setCadastralFilter(this);;
	}
}
