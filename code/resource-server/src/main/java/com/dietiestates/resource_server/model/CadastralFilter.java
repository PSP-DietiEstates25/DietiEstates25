package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.model.range.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
	
	@OneToOne(mappedBy = "cadastralFilter", cascade = CascadeType.ALL, orphanRemoval = true)
	private Search search;
	
	@Builder(builderMethodName = "builder")
	public CadastralFilter(
		BigDecimal minPrice,
		BigDecimal maxPrice,
		Integer minSquareMeters,
		Integer maxSquareMeters,
		Integer minEnergyClass,
		Integer maxEnergyClass,
		Integer minRooms,
		Integer maxRooms,
		Integer minFloor,
		Integer maxFloor
		) {
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
	}
	
}
