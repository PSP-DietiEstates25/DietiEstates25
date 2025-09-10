package com.dietiestates.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RealEstateDto {
	
	//@NotBlank(message = "Id is mandatory!")
	//private long id;
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotEmpty(message = "Images are mandatory")
	@NotBlank(message = "Images are mandatory")
	private String[] images;
	
	@NotEmpty(message = "Description is mandatory")
	@NotBlank(message = "Description is mandatory")
	@Size(min = 1, message = "Description must be a maximum of 200 characters long")
	@Size(max = 200, message = "Description must be at least 1 character long")
	private String description;
	
	@NotEmpty(message = "Price is mandatory")
	@NotBlank(message = "Price is mandatory")
	@Positive
	private BigDecimal price;
	
	@NotEmpty(message = "Size is mandatory")
	@NotBlank(message = "Size is mandatory")
	@Digits(fraction = 2, integer = 3)
	@Positive
	private Float size;
	
	@NotEmpty(message = "Address is mandatory")
	@NotBlank(message = "Address is mandatory")
	@Size(min = 1, message = "Address must be a maximum of 100 characters long")
	@Size(max = 100, message = "Address must be at least 1 character long")
	private String address;
	
	@NotEmpty(message = "Rooms number is mandatory")
	@NotBlank(message = "Rooms number is mandatory")
	@Positive
	private Integer rooms;
	
	@NotEmpty(message = "Floor is mandatory")
	@NotBlank(message = "Floor is mandatory")
	@Positive
	private Integer floor;
	
	@NotEmpty(message = "Energy class is mandatory")
	@NotBlank(message = "Energy class is mandatory")
	private String energyClass;
	
	@NotEmpty(message = "Latitude is mandatory")
	@NotBlank(message = "Latitude is mandatory")
	@Min(value = -180)
	private Double latitude;
	
	@NotEmpty(message = "Longitude is mandatory")
	@NotBlank(message = "Longitude is mandatory")
	@Min(value = -90)
	private Double longitude;

}
