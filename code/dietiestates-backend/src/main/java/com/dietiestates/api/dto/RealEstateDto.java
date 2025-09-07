package com.dietiestates.api.dto;

import java.math.BigDecimal;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RealEstateDto {
	
	@NotBlank(message = "Id is mandatory!")
	private long id;
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotEmpty(message = "Images are mandatory")
	@NotBlank(message = "Images are mandatory")
	private String[] images;
	
	@NotEmpty(message = "Description is mandatory")
	@NotBlank(message = "Description is mandatory")
	private String description;
	
	@NotEmpty(message = "Price is mandatory")
	@NotBlank(message = "Price is mandatory")
	private BigDecimal price;
	
	@NotEmpty(message = "Size is mandatory")
	@NotBlank(message = "Size is mandatory")
	private Double size;
	
	@NotEmpty(message = "Address is mandatory")
	@NotBlank(message = "Address is mandatory")
	private String address;
	
	@NotEmpty(message = "Rooms number is mandatory")
	@NotBlank(message = "Rooms number is mandatory")
	private Integer roomsNumber;
	
	@NotEmpty(message = "Floor is mandatory")
	@NotBlank(message = "Floor is mandatory")
	private Integer floor;
	
	@NotEmpty(message = "Energy class is mandatory")
	@NotBlank(message = "Energy class is mandatory")
	private String energyClass;
	
	@NotEmpty(message = "Latitude is mandatory")
	@NotBlank(message = "Latitude is mandatory")
	private Double latitude;
	
	@NotEmpty(message = "Longitude is mandatory")
	@NotBlank(message = "Longitude is mandatory")
	private Double longitude;

}
