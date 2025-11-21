package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RealEstateRequest {

    @NotEmpty(message = "Category is mandatory")
    @NotBlank(message = "Category is mandatory")
    private String category;

    @NotEmpty(message = "Description is mandatory")
    @NotBlank(message = "Description is mandatory")
    @Size(min = 1, message = "Description must be at least 1 character long")
    @Size(max = 200, message = "Description must be a maximum of 200 characters long")
    private String description;

    @Positive(message = "Cadastral data id must be a positive number")
    private Long cadastralDataId;

    @Positive(message = "Detail id must be a positive number")
    private Long detailId;
}
