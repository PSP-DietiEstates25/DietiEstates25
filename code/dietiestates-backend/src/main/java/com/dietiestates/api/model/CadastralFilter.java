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
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CadastralFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		this.priceRange = PriceRange.builder()
				.minPrice(minPrice)
				.maxPrice(maxPrice)
				.build();
		
		this.squareMetersRange = SquareMetersRange.builder()
				.minSquareMeters(minSquareMeters)
				.maxSquareMeters(maxSquareMeters)
				.build();
		
		this.energyClassRange = EnergyClassRange.builder()
				.minEnergyClass(minEnergyClass)
				.maxEnergyClass(maxEnergyClass)
				.build();
		
		this.roomsRange = RoomsRange.builder()
				.minRooms(minRooms)
				.maxRooms(maxRooms)
				.build();
		
		this.floorRange = FloorRange.builder()
				.minFloor(minFloor)
				.maxFloor(maxFloor)
				.build();
		
		this.setSearch(search);
	}
	
	public void setSearch(Search search) {
		this.search = search;
		search.setCadastralFilter(this);;
	}
}
