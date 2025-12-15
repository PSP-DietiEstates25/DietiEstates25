package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GeographicalPositionRequest {

    @NotEmpty(message = "City is mandatory")
    @NotBlank(message = "City is mandatory")
    private String city;

    @NotEmpty(message = "Region is mandatory")
    @NotBlank(message = "Region is mandatory")
    private String region;

    @NotEmpty(message = "Municipality is mandatory")
    @NotBlank(message = "Municipality is mandatory")
    private String municipality;

    @NotEmpty(message = "Address is mandatory")
    @NotBlank(message = "Address is mandatory")
    @Size(min = 1, message = "Address must be a maximum of 100 characters long")
    @Size(max = 100, message = "Address must be at least 1 character long")
    private String address;

    @NotNull(message = "Latitude is mandatory")
    @Min(value = -180)
    private Double latitude;

    @NotNull(message = "Longitude is mandatory")
    @Min(value = -90)
    private Double longitude;

    @Positive(message = "Radius must be a positive number")
    private Integer radius;
}
