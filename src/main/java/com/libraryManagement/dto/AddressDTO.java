package com.libraryManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {

    private Long id;

    @NotBlank(message = "Street is required")
    @Size(max = 30, message = "Street cannot exceed 30 characters")
    private String street;

    @NotBlank(message = "Number is required")
    @Size(max = 10, message = "Number cannot exceed 10 characters")
    private String number;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "Province cannot exceed 50 characters")
    private String province;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;

    @NotBlank(message = "Postal code is required")
    @Size(max = 10, message = "Postal code cannot exceed 10 characters")
    @Pattern(regexp = "^[0-9A-Za-z\\s-]+$", message = "Invalid postal code format")
    @JsonProperty("postal_code")
    private String postalCode;
}